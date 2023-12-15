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

            implementation(libs.database.cache4k)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.uuid)
        }

        commonTest.dependencies {
            implementation(project(":comments-repo-tests"))
        }

        jvmMain.dependencies {
            implementation(kotlin("stdlib"))
        }
    }
}