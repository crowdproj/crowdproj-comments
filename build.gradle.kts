import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") apply false
    kotlin("multiplatform") apply false
}

group = "com.crowdproj.product.comments"
version = "0.0.1"

val javaVersion: String by project

allprojects {
    repositories{
        mavenCentral()
    }
}

subprojects {
    group = rootProject.group
    version = rootProject.version

    repositories {
        mavenCentral()
    }
    tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = javaVersion
    }
}