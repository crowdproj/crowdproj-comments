package com.crowdproj.comments.biz.validation

import com.crowdproj.comments.common.CommentContext
import com.crowdproj.comments.common.helpers.errorValidation
import com.crowdproj.comments.common.helpers.fail
import com.crowdproj.kotlin.cor.handlers.CorChainDsl
import com.crowdproj.kotlin.cor.handlers.CorWorkerDsl
import com.crowdproj.kotlin.cor.handlers.worker

fun CorChainDsl<CommentContext>.validateCommentIdProperFormat(title: String) = worker {
    validateIdProperFormat(
        title = title,
        regExp = Regex("^[0-9a-zA-Z-]*\$"),
        idType = IdType.CommentId,
    )
}

fun CorChainDsl<CommentContext>.validateObjectIdProperFormat(title: String) = worker {
    validateIdProperFormat(
        title = title,
        regExp = Regex("^[0-9a-zA-Z-]*\$"),
        idType = IdType.ObjectId,
    )
}

fun CorChainDsl<CommentContext>.validateUserIdProperFormat(title: String) = worker {
    validateIdProperFormat(
        title = title,
        regExp = Regex("^[0-9a-zA-Z-]*\$"),
        idType = IdType.UserId,
    )
}

private enum class IdType(val field: String) {
    CommentId("id"),
    ObjectId("objectId"),
    UserId("userId")
}

private fun CorWorkerDsl<CommentContext>.validateIdProperFormat(title: String, regExp: Regex, idType: IdType) {
    this.title = title
    on {
        when (idType) {
            IdType.CommentId -> !commentValidating.id.asString().matches(regExp)
            IdType.ObjectId -> !commentValidating.objectId.asString().matches(regExp)
            IdType.UserId -> !commentValidating.userId.asString().matches(regExp)
        }
    }
    handle {
        val encodedId = when (idType) {
            IdType.CommentId -> commentValidating.id.asString()
            IdType.ObjectId -> commentValidating.objectId.asString()
            IdType.UserId -> commentValidating.userId.asString()
        }.replace("<", "&lt;").replace(">", "&gt;")

        fail(
            errorValidation(
                field = idType.field,
                violationCode = "wrongFormat",
                description = "Value $encodedId must contain only letters and numbers",
            )
        )
    }
}