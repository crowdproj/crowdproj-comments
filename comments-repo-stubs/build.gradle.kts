plugins {
    alias(libs.plugins.kotlin.multiplatform)
}

kotlin {
    jvm {}
    linuxX64 {}
    linuxArm64 {}

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":comments-common"))
                implementation(project(":comments-stubs"))
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
                implementation(kotlin("stdlib"))
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
            }
        }
    }
}