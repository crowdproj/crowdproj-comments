package com.crowdproj.comments.app

import com.crowdproj.comments.app.configs.CommentsAppSettings
import com.crowdproj.comments.app.plugins.configureRouting
import com.crowdproj.comments.app.plugins.initAppSettings
import com.crowdproj.comments.app.plugins.initCors
import com.crowdproj.comments.app.plugins.initRest
import io.ktor.server.application.*

fun Application.moduleJvm(appSettings: CommentsAppSettings = initAppSettings()) {
    println("CONFIGS ${environment.config.toMap().size}")
    environment.config.toMap().forEach {
        println("${it.key} -> ${it.value}")
    }
    initRest(appSettings)
    initCors(appSettings)
    configureRouting(appSettings)
}