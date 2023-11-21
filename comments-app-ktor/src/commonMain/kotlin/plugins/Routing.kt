package com.crowdproj.comments.app.plugins

import com.crowdproj.comments.app.configs.CommentsAppSettings
import com.crowdproj.comments.api.v1.models.*
import com.crowdproj.comments.app.controllers.controllerHelperV1
import com.crowdproj.comments.app.controllers.wsHandler
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
                call.controllerHelperV1<CommentCreateRequest, CommentCreateResponse>(appSettings)
            }
            post("read") {
                call.controllerHelperV1<CommentReadRequest, CommentReadResponse>(appSettings)
            }
            post("update") {
                call.controllerHelperV1<CommentUpdateRequest, CommentUpdateResponse>(appSettings)
            }
            post("delete") {
                call.controllerHelperV1<CommentDeleteRequest, CommentDeleteResponse>(appSettings)
            }
            post("search") {
                call.controllerHelperV1<CommentSearchRequest, CommentSearchResponse>(appSettings)
            }
            webSocket("ws") {
                wsHandler(appSettings)
            }
        }

    }
}

