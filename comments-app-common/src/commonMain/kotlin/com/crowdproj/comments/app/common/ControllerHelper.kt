package com.crowdproj.comments.app.common

import com.crowdproj.comments.common.CommentContext
import com.crowdproj.comments.common.helpers.asCommentError
import com.crowdproj.comments.common.models.CommentState
import com.crowdproj.comments.mappers.log.toLog
import kotlinx.datetime.Clock
import kotlin.reflect.KFunction

suspend inline fun <T> ICommentsAppSettings.controllerHelper(
    crossinline setRequest: suspend CommentContext.() -> Unit,
    crossinline toResponse: suspend CommentContext.() -> T,
    cls: KFunction<*>,
    logId:String
): T {
    val logger = corSettings.loggerProvider.logger(cls)
    val ctx = CommentContext(
        timeStarted = Clock.System.now()
    )
    return try {
        logger.doWithLogging(logId) {
            ctx.setRequest()
            processor.exec(ctx)
            logger.info(
                msg = "Request $logId processed for ${cls.name}",
                marker = "BIZ",
                data = ctx.toLog(logId)
            )
            ctx.toResponse()
        }
    } catch (e: Throwable) {
        ctx.state = CommentState.FAILING
        ctx.errors += e.asCommentError()
        processor.exec(ctx)
        ctx.toResponse()
    }
}