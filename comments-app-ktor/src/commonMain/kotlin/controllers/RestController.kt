package com.crowdproj.comments.app.controllers

import com.crowdproj.comments.api.v1.models.ICommentRequest
import com.crowdproj.comments.api.v1.models.ICommentResponse
import com.crowdproj.comments.app.common.controllerHelper
import com.crowdproj.comments.app.configs.CommentsAppSettings
import com.crowdproj.comments.common.models.CommentRequestId
import com.crowdproj.comments.mappers.v1.fromTransport
import com.crowdproj.comments.mappers.v1.toTransport
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.callid.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.utils.io.charsets.*
import kotlinx.datetime.Clock

suspend inline fun <reified Rq : ICommentRequest, reified Rs : ICommentResponse> ApplicationCall.controllerHelperV1(
    appSettings: CommentsAppSettings
) {
    val endpoint: String = this.request.local.localAddress
    val requestId = this.callId
    val logger = application.log
    suitableCharset(Charsets.UTF_8)
    defaultTextContentType(ContentType.Application.Json.withCharset(Charsets.UTF_8))

    logger.info("Started $endpoint request $requestId")
    try {
        appSettings.controllerHelper(
            {
                this.timeStarted = Clock.System.now()
                this.requestId = requestId?.let { CommentRequestId(it) } ?: CommentRequestId.NONE
                fromTransport(receive<Rq>())
            },
            {
                respond<Rs>(toTransport() as Rs)
            }
        )
    } catch (e: Throwable) {
        logger.error(
            "Fail to handle $endpoint request $requestId with exception", e
        )
    }
}