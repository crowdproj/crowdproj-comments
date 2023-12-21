plugins {
    alias(libs.plugins.kotlin.multiplatform)
}

kotlin {
    jvm {}
    linuxX64 {}
    linuxArm64 {}

    sourceSets {

        all { languageSettings.optIn("kotlin.RequiresOptIn") }

        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))

                implementation(project(":comments-common"))
                implementation(project(":comments-stubs"))
                implementation(libs.crowdproj.cor)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
                implementation(project(":comments-repo-stubs"))

                api(libs.kotlinx.coroutines.test)
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