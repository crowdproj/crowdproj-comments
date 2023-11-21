package com.crowdproj.comments.app.common

import com.crowdproj.comments.common.CommentContext
import com.crowdproj.comments.common.helpers.asCommentError
import com.crowdproj.comments.common.models.CommentState
import kotlinx.datetime.Clock

suspend inline fun <T> ICommentsAppSettings.controllerHelper(
    setRequest: CommentContext.() -> Unit,
    toResponse: CommentContext.() -> T
): T {
    val ctx = CommentContext(
        timeStarted = Clock.System.now()
    )
    return try {
        ctx.setRequest()
        processor.exec(ctx)
        ctx.toResponse()
    } catch (e: Throwable) {
        ctx.state = CommentState.FAILING
        ctx.errors += e.asCommentError()
        processor.exec(ctx)
        ctx.toResponse()
    }
}