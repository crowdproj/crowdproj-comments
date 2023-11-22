plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
}

group = "com.crowdproj.comments"
version = System.getenv("PROJECT_VERSION") ?: "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
    maven { url = uri("<https://maven.pkg.jetbrains.space/public/p/ktor/eap>") }
}

subprojects {
    group = rootProject.group
    version = rootProject.version

    repositories {
        mavenCentral()
        maven { url = uri("<https://maven.pkg.jetbrains.space/public/p/ktor/eap>") }
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