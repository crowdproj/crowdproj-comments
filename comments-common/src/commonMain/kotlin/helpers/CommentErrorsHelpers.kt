package com.crowdproj.comments.common.helpers

import com.crowdproj.comments.common.CommentContext
import com.crowdproj.comments.common.models.CommentError
import com.crowdproj.comments.common.models.CommentState

fun Throwable.asCommentError(
    code: String = "unknown",
    group: String = "exceptions",
    message: String = this.message ?: "",
) = CommentError(
    code = code,
    group = group,
    field = "",
    message = message,
    exception = this,
)
fun CommentContext.addError(vararg error: CommentError) = errors.addAll(error)
fun CommentContext.fail(error: CommentError) {
    addError(error)
    state = CommentState.FAILING
}