package com.crowdproj.comments.common.helpers

import com.crowdproj.comments.common.CommentContext
import com.crowdproj.comments.common.models.CommentError
import com.crowdproj.comments.common.models.CommentState

fun Throwable.asCommentError(
    code: String = "unknown",
    group: CommentError.Group = CommentError.Group.NONE,
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

fun errorValidation(
    field: String,
    /**
     * Code describing the error. Must not include field name or validation indication.
     * Example: empty, badSymbols, tooLong, etc
     */
    violationCode: String,
    description: String,
    level: CommentError.Level = CommentError.Level.ERROR,
) = CommentError(
    code = "validation-$field-$violationCode",
    field = field,
    group = CommentError.Group.VALIDATION,
    message = "Validation error in field $field: $description",
    level = level
)