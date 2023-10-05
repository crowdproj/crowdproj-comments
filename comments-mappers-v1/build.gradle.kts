plugins {
    kotlin("multiplatform")
}

kotlin {
    jvm { }
    linuxX64 { }

    sourceSets {
        val commonMain by getting {

            kotlin.srcDirs(layout.buildDirectory.dir("generate-resources/main/src/commonMain/kotlin"))
            dependencies {
                implementation(kotlin("stdlib-common"))

                implementation(project(":comments-api-v1"))
                implementation(project(":comments-common"))
            }
        }
    }
}