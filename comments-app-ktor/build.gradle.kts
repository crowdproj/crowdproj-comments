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

ktor {
    docker {
        jreVersion.set(JavaVersion.VERSION_17)
        localImageName.set(project.name)
        imageTag.set("${project.version}")
        portMappings.set(
            listOf(
                io.ktor.plugin.features.DockerPortMapping(
                    80,
                    8080,
                    io.ktor.plugin.features.DockerPortMappingProtocol.TCP
                )
            )
        )

//        externalRegistry.set(
//            io.ktor.plugin.features.DockerImageRegistry.dockerHub(
//                appName = provider { "ktor-app" },
//                username = providers.environmentVariable("DOCKER_HUB_USERNAME"),
//                password = providers.environmentVariable("DOCKER_HUB_PASSWORD")
//            )
//        )
    }

}

tasks {
    val linkReleaseExecutableLinuxX64 by getting(KotlinNativeLink::class)
    val nativeFile = linkReleaseExecutableLinuxX64.binary.outputFile
//    val linkDebugExecutableLinuxX64 by getting(KotlinNativeLink::class)
//    val nativeFile = linkDebugExecutableLinuxX64.binary.outputFile
    val linuxX64ProcessResources by getting(ProcessResources::class)

    val dockerDockerfile by creating(Dockerfile::class) {
        dependsOn(linkReleaseExecutableLinuxX64)
        dependsOn(linuxX64ProcessResources)
        group = "docker"
        from("ubuntu:23.04")
        doFirst {
            copy {
                from(nativeFile)
                from(linuxX64ProcessResources.destinationDir)
                into("${this@creating.destDir.get()}")
            }
        }
        copyFile(nativeFile.name, "/app/")
        copyFile("application.yaml", "/app/")
        exposePort(8081)
        workingDir("/app")
        entryPoint("/app/${nativeFile.name}", "-config=./application.yaml")
    }
    val registryUser: String? = System.getenv("CONTAINER_REGISTRY_USER")
    val registryPass: String? = System.getenv("CONTAINER_REGISTRY_PASS")
    val registryHost: String? = System.getenv("CONTAINER_REGISTRY_HOST")
    val registryPref: String? = System.getenv("CONTAINER_REGISTRY_PREF")
    val imageName = registryPref?.let { "$it/${project.name}" } ?: project.name

    val dockerBuildNativeImage by creating(DockerBuildImage::class) {
        group = "docker"
        dependsOn(dockerDockerfile)
        images.add("$imageName:${project.version}")
        images.add("$imageName:latest")
    }
    val dockerPushNativeImage by creating(DockerPushImage::class) {
        group = "docker"
        dependsOn(dockerBuildNativeImage)
        images.set(dockerBuildNativeImage.images)
        registryCredentials {
            username.set(registryUser)
            password.set(registryPass)
            url.set("https://$registryHost/v1/")
        }
    }

    create("deploy") {
        group = "build"
        dependsOn(dockerPushNativeImage)
    }
}

tasks.withType<Copy> {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}