package com.crowdproj.comments.app.plugins

import com.crowdproj.comments.app.configs.CommentsAppSettings
import com.crowdproj.comments.api.v1.models.*
import helpers.controllerHelperV1
import io.ktor.server.application.*
import io.ktor.server.routing.*
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlin.reflect.KClass

private val clazz: KClass<*> = Application::configureRouting::class

@OptIn(ExperimentalEncodingApi::class)
fun Application.configureRouting(appConfig: CommentsAppSettings) {
    initRest(appConfig)
    initCors(appConfig)
    routing {

        swagger(appConfig)
        route("v1") {
            post("create") {
                call.controllerHelperV1<CommentCreateRequest, CommentCreateResponse>(appConfig)
            }
            post("read") {
                call.controllerHelperV1<CommentReadRequest, CommentReadResponse>(appConfig)
            }
            post("update") {
                call.controllerHelperV1<CommentUpdateRequest, CommentUpdateResponse>(appConfig)
            }
            post("delete") {
                call.controllerHelperV1<CommentDeleteRequest, CommentDeleteResponse>(appConfig)
            }
            post("search") {
                call.controllerHelperV1<CommentSearchRequest, CommentSearchResponse>(appConfig)
            }
        }
    }
}

