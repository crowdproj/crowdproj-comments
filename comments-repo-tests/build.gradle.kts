plugins {
    alias(libs.plugins.kotlin.multiplatform)
}

kotlin {
    jvm {}
    linuxX64 {}
    linuxArm64 {}

    sourceSets {
        commonMain.dependencies {
            implementation(project(":comments-common"))

            api(kotlin("test-common"))
            api(kotlin("test-annotations-common"))

            api(libs.kotlinx.coroutines.core)
            api(libs.kotlinx.coroutines.test)
        }

        jvmMain.dependencies {
            api(kotlin("test-junit"))
        }
    }
}