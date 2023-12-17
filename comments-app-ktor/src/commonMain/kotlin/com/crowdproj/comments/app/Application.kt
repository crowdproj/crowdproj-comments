package com.crowdproj.comments.app

import com.crowdproj.comments.app.configs.CommentsAppSettings
import com.crowdproj.comments.app.plugins.configureRouting
import com.crowdproj.comments.app.plugins.initAppSettings
import io.ktor.server.application.*

expect fun main(args: Array<String>)

fun Application.module(appSettings: CommentsAppSettings = initAppSettings()) {
    println("CONFIGS ${environment.config.toMap().size}")
    environment.config.toMap().forEach {
        println("${it.key} -> ${it.value}")
    }
    configureRouting(appSettings)
}