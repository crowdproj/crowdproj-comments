rootProject.name = "crowdproj-comments"

pluginManagement {
    val kotlinJvmVersion : String by settings
    plugins {
        kotlin("jvm") version kotlinJvmVersion
    }
}

include("m1l1-quikstart")

