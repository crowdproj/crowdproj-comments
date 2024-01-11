plugins{
    alias(libs.plugins.kotlin.multiplatform)
}

kotlin {
    jvm {}
    linuxX64 {}
    linuxArm64 {}

    sourceSets {
        val datetimeVersion: String by project
        val coroutinesVersion: String by project

        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))

                api(libs.kotlinx.coroutines.core)
                api(libs.kotlinx.datetime)

                implementation(project(":comments-common"))
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
    }
}