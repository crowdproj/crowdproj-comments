rootProject.name = "crowdproj-comments"

pluginManagement {
    plugins {
        val kotlinVersion: String by settings
        val codeGeneratorVersion: String by settings

        kotlin("jvm") version kotlinVersion apply false
        kotlin("multiplatform") version kotlinVersion
        kotlin("plugin.serialization") version kotlinVersion apply false

        id("com.crowdproj.generator") version codeGeneratorVersion apply false
    }
}

include("comments-api-v1")
include("comments-common")
include("comments-mappers-v1")