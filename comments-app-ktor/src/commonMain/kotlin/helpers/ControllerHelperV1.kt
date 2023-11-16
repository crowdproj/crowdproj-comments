package helpers

import com.crowdproj.comments.app.configs.CommentsAppSettings
import com.crowdproj.comments.common.CommentContext
import com.crowdproj.comments.common.helpers.asCommentError
import com.crowdproj.comments.common.helpers.fail
import com.crowdproj.comments.common.models.CommentRequestId
import com.crowdproj.comments.api.v1.models.ICommentRequest
import com.crowdproj.comments.api.v1.models.ICommentResponse
import fromTransport
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.callid.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.utils.io.charsets.*
import kotlinx.datetime.Clock
import toTransport

suspend inline fun <reified Rq : ICommentRequest, reified Rs : ICommentResponse> ApplicationCall.controllerHelperV1(
    appConfig: CommentsAppSettings
) {
    val endpoint: String = this.request.local.localAddress
    val requestId = this.callId
    val logger = application.log
    suitableCharset(Charsets.UTF_8)
    defaultTextContentType(ContentType.Application.Json.withCharset(Charsets.UTF_8))
    val ctx = CommentContext(
        timeStarted = Clock.System.now(),
        requestId = requestId?.let { CommentRequestId(it) } ?: CommentRequestId.NONE,
    )
    try {
        logger.info("Started $endpoint request $requestId")
        val reqData = this.receive<Rq>()
        ctx.fromTransport(reqData)
        appConfig.processor.exec(ctx)
        respond<Rs>(ctx.toTransport() as Rs)
        logger.info("Finished $endpoint request $requestId")
    } catch (e: BadRequestException) {
        logger.error(
            "Bad request $requestId at $endpoint with exception", e
        )
        ctx.fail(
            e.asCommentError(
                code = "bad-request",
                group = "bad-request",
                message = "The request is not correct due to wrong/missing request parameters, body content or header values"
            )
        )
        appConfig.processor.exec(ctx)
        respond<Rs>(ctx.toTransport() as Rs)
    } catch (e: Throwable) {
        logger.error(
            "Fail to handle $endpoint request $requestId with exception", e
        )
        ctx.fail(
            e.asCommentError(
                message = "Unknown error. We have been informed and dealing with the problem."
            )
        )
        //appConfig.processor.exec(ctx)
        respond<Rs>(ctx.toTransport() as Rs)
    }
}