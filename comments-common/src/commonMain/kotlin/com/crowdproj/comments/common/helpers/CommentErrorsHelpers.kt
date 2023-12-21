package com.crowdproj.comments.common.helpers

import com.crowdproj.comments.common.CommentContext
import com.crowdproj.comments.common.exceptions.RepoConcurrencyException
import com.crowdproj.comments.common.models.CommentError
import com.crowdproj.comments.common.models.CommentLock
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
    state = CommentState.FAILING
    addError(error)
}

fun CommentContext.fail(errors: List<CommentError>) {
    state = CommentState.FAILING
    addError(*errors.toTypedArray())
}

fun errorValidation(
    field: String,
    /**
     * Code describing the error. Must not include field name or validation indication.
     * Example: empty, badSymbols, tooLong, etc
     */
    violationCode: String,
    description: String,
    level: CommentError.Level = CommentError.Level.INFO,
) = CommentError(
    code = "validation-$field-$violationCode",
    field = field,
    group = CommentError.Group.VALIDATION,
    message = "Validation error in field $field: $description",
    level = level
)

fun errorAdministration(
    /**
     * Код, характеризующий ошибку. Не должен включать имя поля или указание на валидацию.
     * Например: empty, badSymbols, tooLong, etc
     */
    field: String = "",
    violationCode: String,
    description: String,
    exception: Exception? = null,
    level: CommentError.Level = CommentError.Level.ERROR,
) = CommentError(
    field = field,
    code = "administration-$violationCode",
    group = CommentError.Group.ADMINISTRATION,
    message = "Microservice management error: $description",
    level = level,
    exception = exception,
)

fun errorRepoConcurrency(
    expectedLock: CommentLock,
    actualLock: CommentLock?,
    exception: Exception? = null,
) = CommentError(
    code = "concurrency",
    group = CommentError.Group.REPOSITORY,
    field = "lock",
    message = "The object has been changed concurrently by another user or process",
    exception = exception ?: RepoConcurrencyException(expectedLock, actualLock)
)