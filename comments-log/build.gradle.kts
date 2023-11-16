plugins {
    kotlin("multiplatform")
}

version = rootProject.version

kotlin {
    jvm { withJava() }
    linuxX64 { }

    sourceSets {

        val commonMain by getting {

            dependencies {
                implementation(kotlin("stdlib-common"))
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
                implementation(libs.logback.classic)
                implementation(libs.logback.access)

                implementation(libs.slf4j.api)
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
            }
        }
    }
}
