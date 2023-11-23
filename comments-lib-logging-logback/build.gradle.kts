plugins {
    alias(libs.plugins.kotlin.jvm)
}

group = rootProject.group
version = rootProject.version

dependencies {

    implementation(kotlin("stdlib-jdk8"))

    implementation(project(":comments-lib-logging-common"))

    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.datetime)

    // logback
    implementation(libs.logging.logback.encoder)
//    implementation(libs.logging.logback.kafka)
    implementation(libs.janino)
    api(libs.logging.logback.classic)

    testImplementation(kotlin("test-junit"))
}