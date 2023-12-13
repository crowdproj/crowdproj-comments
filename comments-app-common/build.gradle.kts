plugins {
    kotlin("multiplatform")
}

kotlin {
    jvm { }
    linuxX64 { }
    linuxArm64 { }

    sourceSets {
        val coroutinesVersion: String by project
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-jdk8"))

                // transport models
                implementation(project(":comments-common"))

                //logging
                implementation(project(":comments-mappers-log"))

                // Biz
                implementation(project(":comments-biz"))
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))

                // transport models
                implementation(project(":comments-api-v1"))
                implementation(project(":comments-mappers-v1"))
                implementation(project(":comments-api-log"))

                implementation(libs.kotlinx.coroutines.test)
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
            }
        }
        val linuxX64Test by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}

