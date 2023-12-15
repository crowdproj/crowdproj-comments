plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.kapt)
}

dependencies {
    implementation(project(":comments-common"))

    implementation(libs.kotlin.stdlib)

    implementation(libs.kotlinx.coroutines.jdk9)

    implementation(libs.uuid)

    implementation(libs.database.datastax.core)
    implementation(libs.database.datastax.query)
    kapt(libs.database.datastax.mapper.processor)
    implementation(libs.database.datastax.mapper.runtime)

    //log
    implementation(libs.logging.logback.classic)

    testImplementation(project(":comments-repo-tests"))
    testImplementation(libs.testcontainers.cassandra)

}
