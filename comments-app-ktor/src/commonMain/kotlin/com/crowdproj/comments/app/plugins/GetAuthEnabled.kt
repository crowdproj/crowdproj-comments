package com.crowdproj.comments.app.plugins

import com.crowdproj.comments.app.configs.ConfigPaths
import io.ktor.server.application.*

fun Application.getAuthEnabled(): Boolean {

    val authConfig = environment.config.config(ConfigPaths.auth)
    val authDisabled = authConfig.propertyOrNull("enabled")?.getString()?.lowercase()

    return when(authDisabled) {
        "false" -> false
        else -> true
    }
}