import org.jetbrains.kotlin.incremental.createDirectory
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

plugins {
    kotlin("multiplatform")
}

version = rootProject.version

val apiVersion = "v1"
val apiSpec: Configuration by configurations.creating
dependencies {
    apiSpec(
        group = "com.crowdproj",
        name = "specs-v1",
        version = libs.versions.apiSpec.get(),
        classifier = "openapi",
        ext = "yaml"
    )
}

val embeddings = layout.buildDirectory.dir("generate-resources/main/src/commonMain/kotlin").get()

kotlin {
    jvm { withJava() }
    linuxX64 { }
    linuxArm64 {}

    sourceSets {

        val commonMain by getting {

            kotlin.srcDirs(embeddings)
            dependencies {
                implementation(kotlin("stdlib-common"))

                implementation(libs.kotlinx.serialization.core)
                implementation(libs.kotlinx.serialization.json)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }

        val jvmMain by getting {
            dependencies {
            }
        }
    }
}

tasks {

    val prepareSwagger by creating(Copy::class) {
        group = "swagger"
        destinationDir = file("${layout.buildDirectory.get()}/swagger")
//    dependsOn(apiSpec.asPath)
        from("$rootDir/specs") {
            into("specs")
            filter {
                // Устанавливаем версию в сваггере
                it.replace("\${VERSION_APP}", project.version.toString())
            }
        }
        from(apiSpec) {
            into("specs")
            rename { "base.yaml" }
        }
        outputs.dir(destinationDir)
    }

    val generateResourceKt by creating {
        group = "swagger"
        dependsOn(prepareSwagger)
        file(embeddings).createDirectory()
        val resPath = prepareSwagger.destinationDir
        inputs.dir(resPath)
        var cntr = 0
        doLast {
            @OptIn(ExperimentalEncodingApi::class)
            val resources = fileTree(resPath).files
                .map { fileContent ->
                    file("$embeddings/Resource_${cntr}.kt").apply(File::createNewFile).writeText(
                        """
                                package com.crowdproj.comments.app.resources

                                val RES_${cntr} = "${Base64.encode(fileContent.readBytes())}"
                            """.trimIndent()
                    )
                    fileContent.relativeTo(resPath).toString() to cntr++
                }
            file("$embeddings/Resources.kt").apply(File::createNewFile).writeText(
                """
                        package com.crowdproj.comments.app.resources

                        val RESOURCES = mapOf(
                            ${
                    resources.joinToString(",\n                            ") {
                        "\"${it.first.replace("\\", "/")}\" to RES_${it.second}"
                    }
                }
                        )
                    """.trimIndent()
            )
        }
    }

    filter { it.name.startsWith("compile") }.forEach {
        it.dependsOn(generateResourceKt)
    }
}

