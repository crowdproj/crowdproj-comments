package com.crowdproj.comments.app.plugins

import com.crowdproj.comments.app.configs.CommentsAppSettings
import com.crowdproj.comments.app.controllers.*
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import kotlin.io.encoding.ExperimentalEncodingApi

@OptIn(ExperimentalEncodingApi::class)
fun Application.configureRouting(appSettings: CommentsAppSettings) {
    routing {
        swagger(appSettings)
        route("v1") {
            post("create") {
                call.createComment(appSettings)
            }
            post("read") {
                call.readComment(appSettings)
            }
            post("update") {
                call.updateComment(appSettings)
            }
            post("delete") {
                call.deleteComment(appSettings)
            }
            post("search") {
                call.searchComment(appSettings)
            }
            webSocket("ws") {
                //this.call.request.headers.
                wsHandler(appSettings)
            }
        }
    }
}

