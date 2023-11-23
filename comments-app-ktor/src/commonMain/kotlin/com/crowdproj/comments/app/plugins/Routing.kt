package com.crowdproj.comments.app.plugins

import com.crowdproj.comments.app.configs.CommentsAppSettings
import com.crowdproj.comments.app.controllers.*
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import kotlin.io.encoding.ExperimentalEncodingApi

@OptIn(ExperimentalEncodingApi::class)
fun Application.configureRouting(appSettings: CommentsAppSettings) {
    initRest(appSettings)
    initCors(appSettings)
    install(WebSockets)
    routing {

        swagger(appSettings)
        route("v1") {
            post("create") {
                call.createAd(appSettings)
            }
            post("read") {
                call.readAd(appSettings)
            }
            post("update") {
                call.updateAd(appSettings)
            }
            post("delete") {
                call.deleteAd(appSettings)
            }
            post("search") {
                call.searchAd(appSettings)
            }
            webSocket("ws") {
                wsHandler(appSettings)
            }
        }

    }
}

