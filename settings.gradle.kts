rootProject.name = "crowdproj-comments"

pluginManagement {
    val kotlinVersion : String by settings
    plugins {
        kotlin("jvm") version kotlinVersion
    }
}

include("m1l1-quikstart")

