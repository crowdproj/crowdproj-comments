rootProject.name = "crowdproj-comments"

pluginManagement {
    plugins {
        val kotlinVersion: String by settings
        val codeGeneratorVersion: String by settings
        val ktorVersion: String by settings
        val openapiVersion: String by settings
        val bmuschkoVersion: String by settings

        kotlin("jvm") version kotlinVersion apply false
        kotlin("multiplatform") version kotlinVersion
        kotlin("plugin.serialization") version kotlinVersion apply false

        id("io.ktor.plugin") version ktorVersion apply false

        id("com.crowdproj.generator") version codeGeneratorVersion apply false
        id("org.openapi.generator") version openapiVersion apply false

        id("com.bmuschko.docker-remote-api") version bmuschkoVersion apply false
    }
}

dependencyResolutionManagement {
    versionCatalogs{
        val ktorVersion: String by settings
        val serializationVersion: String by settings
        create("libs") {
            version("ktor", ktorVersion)
            version("serialization", serializationVersion)

            library("ktor-server-core", "io.ktor" , "ktor-server-core").versionRef("ktor")
            library("ktor-server-core-jvm", "io.ktor" , "ktor-server-core-jvm").versionRef("ktor")
            library("ktor-server-cio", "io.ktor" , "ktor-server-cio").versionRef("ktor")
            library("ktor-server-auth", "io.ktor" , "ktor-server-auth").versionRef("ktor")
            library("ktor-server-auth-jwt", "io.ktor" , "ktor-server-auth-jwt").versionRef("ktor")
            library("ktor-server-auto-head-response", "io.ktor" , "ktor-server-auto-head-response").versionRef("ktor")
            library("ktor-server-caching-headers", "io.ktor" , "ktor-server-caching-headers").versionRef("ktor")
            library("ktor-server-cors", "io.ktor" , "ktor-server-cors").versionRef("ktor")
            library("ktor-server-websockets", "io.ktor" , "ktor-server-websockets").versionRef("ktor")
            library("ktor-server-config-yaml", "io.ktor" , "ktor-server-config-yaml").versionRef("ktor")
            library("ktor-server-content-negotiation", "io.ktor" , "ktor-server-content-negotiation").versionRef("ktor")
            library("ktor-serialization-kotlinx-json", "io.ktor" , "ktor-serialization-kotlinx-json").versionRef("ktor")
            library("ktor-serialization", "io.ktor" , "ktor-serialization").versionRef("ktor")
            library("ktor-server-test-host", "io.ktor" , "ktor-server-test-host").versionRef("ktor")
            library("ktor-server-netty", "io.ktor" , "ktor-server-netty").versionRef("ktor")
            library("ktor-server-locations", "io.ktor" , "ktor-server-locations").versionRef("ktor")
            library("ktor-server-call-logging", "io.ktor" , "ktor-server-call-logging").versionRef("ktor")
            library("ktor-server-default-headers", "io.ktor" , "ktor-server-default-headers").versionRef("ktor")
            library("ktor-server-call-id", "io.ktor" , "ktor-server-call-id").versionRef("ktor")
            library("ktor-server-swagger", "io.ktor" , "ktor-server-swagger").versionRef("ktor")

            library("ktor-serialization-jackson", "io.ktor" , "ktor-serialization-jackson").versionRef("ktor")

            library("ktor-client-content-negotiation", "io.ktor" , "ktor-client-content-negotiation").versionRef("ktor")
            library("ktor-client-websockets", "io.ktor" , "ktor-client-websockets").versionRef("ktor")
            library("ktor-client-core", "io.ktor" , "ktor-client-core").versionRef("ktor")
            library("ktor-client-cio", "io.ktor" , "ktor-client-cio").versionRef("ktor")
            library("ktor-client-mock", "io.ktor", "ktor-client-mock").versionRef("ktor")

            library("kotlinx-serialization-core", "org.jetbrains.kotlinx", "kotlinx-serialization-core").versionRef("serialization")
            library("kotlinx-serialization-json", "org.jetbrains.kotlinx", "kotlinx-serialization-json").versionRef("serialization")
        }
    }
}

include("comments-api-v1")
include("comments-common")
include("comments-mappers-v1")
include("comments-stubs")
include("comments-biz")
include("comments-app-ktor")
include("comments-swagger")
include("comments-log")