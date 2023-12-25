import com.bmuschko.gradle.docker.tasks.image.DockerBuildImage
import com.bmuschko.gradle.docker.tasks.image.DockerPushImage
import com.bmuschko.gradle.docker.tasks.image.Dockerfile
import org.jetbrains.kotlin.gradle.tasks.KotlinNativeLink

plugins {
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.ktor)
    alias(libs.plugins.bmuschko)
}

application {
    mainClass.set("io.ktor.server.cio.EngineMain")
}

kotlin {
    jvm {
        withJava()
    }
    listOf(
        linuxX64 {},
        linuxArm64 {}
    ).forEach {
        it.binaries {
            executable {
                entryPoint = "com.crowdproj.comments.app.main"
            }
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
                api(libs.kotlinx.datetime)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.kotlinx.serialization.core)
                implementation(libs.kotlinx.serialization.json)

                implementation(libs.ktor.serialization.kotlinx.json)
                implementation(libs.ktor.serialization)
                implementation(libs.ktor.server.content.negotiation)
                implementation(libs.ktor.server.auto.head.response)
                implementation(libs.ktor.server.caching.headers)
                implementation(libs.ktor.server.cors)
                implementation(libs.ktor.server.call.id)
                implementation(libs.ktor.server.websockets)
                implementation(libs.ktor.server.config.yaml)
                implementation(libs.ktor.server.core)
                implementation(libs.ktor.server.cio)
                implementation(libs.ktor.server.auth)

                implementation(libs.uuid)

                //logging
                implementation(project(":comments-lib-logging-common"))
                implementation(project(":comments-lib-logging-oshai"))
                implementation(project(":comments-api-log"))
                implementation(project(":comments-mappers-log"))
                implementation(libs.logging.logback.more.appenders)
                implementation(libs.logging.fluent)

                implementation(project(":comments-common"))

                implementation(project(":comments-api-v1"))
                implementation(project(":comments-mappers-v1"))
                implementation(project(":comments-biz"))
                implementation(project(":comments-app-common"))
                implementation(project(":comments-swagger"))

                //repo
                implementation(project(":comments-repo-inmemory"))
                implementation(project(":comments-repo-stubs"))
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))

                implementation(project(":comments-stubs"))

                implementation(libs.ktor.server.test.host)
                implementation(libs.ktor.client.content.negotiation)
                implementation(libs.ktor.client.mock)
                implementation(libs.ktor.client.websockets)
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(libs.ktor.server.swagger)
                implementation(project(":comments-lib-logging-logback"))
                implementation(project(":comments-repo-cassandra"))
            }
        }

        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
                implementation(libs.testcontainers.cassandra)
            }
        }
    }
}


tasks {
    val linkReleaseExecutableLinuxX64 by getting(KotlinNativeLink::class)
    val nativeFileX64 = linkReleaseExecutableLinuxX64.binary.outputFile
    val linkReleaseExecutableLinuxArm64 by getting(KotlinNativeLink::class)
    val nativeFileArm64 = linkReleaseExecutableLinuxArm64.binary.outputFile
//    val linkDebugExecutableLinuxX64 by getting(KotlinNativeLink::class)
//    val nativeFile = linkDebugExecutableLinuxX64.binary.outputFile
    val linuxX64ProcessResources by getting(ProcessResources::class)
    val linuxArm64ProcessResources by getting(ProcessResources::class)
    val dockerLinuxX64Dir = layout.buildDirectory.file("docker-x64/Dockerfile").get().asFile
    val dockerLinuxArm64Dir = layout.buildDirectory.file("docker-arm64/Dockerfile").get().asFile

    val dockerDockerfileX64 by creating(Dockerfile::class) {
        dependsOn(linkReleaseExecutableLinuxX64)
        dependsOn(linuxX64ProcessResources)
        group = "docker"
        destFile.set(dockerLinuxX64Dir)
        from(Dockerfile.From("ubuntu:23.04").withPlatform("linux/amd64"))
        doFirst {
            copy {
                from(nativeFileX64)
                from(linuxX64ProcessResources.destinationDir)
                into("${this@creating.destDir.get()}")
            }
        }
        copyFile(nativeFileX64.name, "/app/")
        copyFile("application.yaml", "/app/")
        exposePort(8081)
        workingDir("/app")
        entryPoint("/app/${nativeFileX64.name}", "-config=./application.yaml")
    }
    val dockerDockerfileArm64 by creating(Dockerfile::class) {
        dependsOn(linkReleaseExecutableLinuxArm64)
        dependsOn(linuxArm64ProcessResources)
        group = "docker"
        destFile.set(dockerLinuxArm64Dir)
        from(Dockerfile.From("ubuntu:23.04").withPlatform("linux/arm64"))
        doFirst {
            copy {
                from(nativeFileArm64)
                from(linuxArm64ProcessResources.destinationDir)
                into("${this@creating.destDir.get()}")
            }
        }
        copyFile(nativeFileArm64.name, "/app/")
        copyFile("application.yaml", "/app/")
        exposePort(8081)
        workingDir("/app")
        entryPoint("/app/${nativeFileArm64.name}", "-config=./application.yaml")
    }
    val registryUser: String? = System.getenv("CONTAINER_REGISTRY_USER")
    val registryPass: String? = System.getenv("CONTAINER_REGISTRY_PASS")
    val registryHost: String? = System.getenv("CONTAINER_REGISTRY_HOST")
    val registryPref: String? = System.getenv("CONTAINER_REGISTRY_PREF")
    val registryName: String? = System.getenv("CONTAINER_REGISTRY_NAME")
    val imageName = registryPref?.let { "$it/$registryName" } ?: registryName

    val dockerBuildX64Image by creating(DockerBuildImage::class) {
        group = "docker"
        dependsOn(dockerDockerfileX64)
        inputDir.set(dockerLinuxX64Dir.parentFile)
        images.add("$imageName:${rootProject.version}-x64")
        images.add("$imageName:latest-x64")
        platform.set("linux/amd64")
    }
    val dockerPushX64Image by creating(DockerPushImage::class) {
        group = "docker"
        dependsOn(dockerBuildX64Image)
        images.set(dockerBuildX64Image.images)
        registryCredentials {
            username.set(registryUser)
            password.set(registryPass)
            url.set("https://$registryHost/v1/")
        }
    }
    val dockerBuildArm64Image by creating(DockerBuildImage::class) {
        group = "docker"
        dependsOn(dockerDockerfileArm64)
        inputDir.set(dockerLinuxArm64Dir.parentFile)
        images.add("$imageName:${rootProject.version}-arm64")
        images.add("$imageName:latest-arm64")
        platform.set("linux/arm64")
    }
    val dockerPushArm64Image by creating(DockerPushImage::class) {
        group = "docker"
        dependsOn(dockerBuildArm64Image)
        images.set(dockerBuildArm64Image.images)
        registryCredentials {
            username.set(registryUser)
            password.set(registryPass)
            url.set("https://$registryHost/v1/")
        }
    }

    create("deploy") {
        group = "build"
        dependsOn(dockerPushX64Image)
        dependsOn(dockerPushArm64Image)
    }
}