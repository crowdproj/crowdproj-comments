plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.crowdproj.generator)
    alias(libs.plugins.kotlinx.serialization)
}

val specDir = "${layout.buildDirectory.get()}/specs"
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

kotlin {
    jvm { withJava() }
    js {
        browser {}
    }
    linuxX64 {}
    linuxArm64 { }

    sourceSets {
        val serializationVersion: String by project

        val commonMain by getting {

            kotlin.srcDirs(layout.buildDirectory.dir("generate-resources/main/src/commonMain/kotlin"))
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

        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
            }
        }
        val jsTest by getting {
            dependencies {
                implementation(kotlin("test-js"))
            }
        }
    }
}

tasks {
    val getSpecs: Task by creating(Copy::class) {
        group = "openapi tools"
        from("${rootProject.projectDir}/specs")
        from(apiSpec) {
            rename { "base.yaml" }
        }
        into(specDir)
    }
    this.openApiGenerate {
        dependsOn(getSpecs)
        mustRunAfter("compileCommonMainKotlinMetadata")
    }
    filter { it.name.startsWith("compile") }.forEach {
        it.dependsOn(openApiGenerate)
    }
}

crowdprojGenerate {
    packageName.set("${project.group}.api.v1")
    inputSpec.set("${specDir}/spec-comments-$apiVersion.yaml")
}

openApiValidate {
    inputSpec.set("${specDir}/spec-comments-$apiVersion.yaml")
}