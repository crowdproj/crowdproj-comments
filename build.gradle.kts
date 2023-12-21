plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.kotlin.kapt) apply false
}

group = "com.crowdproj.comments"
version = System.getenv("PROJECT_VERSION") ?: "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

subprojects {
    group = rootProject.group
    version = rootProject.version

    repositories {
        mavenCentral()
        maven {
            name = "jetbrains"
            url = uri("<https://maven.pkg.jetbrains.space/public/p/ktor/eap>")
        }
        maven {
            name = "gradle"
            url = uri("https://plugins.gradle.org/m2/")
        }
    }
}

tasks {
    val deploy: Task by creating {
        dependsOn("build")
    }
}

afterEvaluate {
    println("VERSION: ${project.version}")
}