package com.crowdproj.comments.biz.validation

import com.crowdproj.comments.common.CommentContext
import com.crowdproj.comments.common.helpers.errorValidation
import com.crowdproj.comments.common.helpers.fail
import com.crowdproj.comments.common.models.CommentObjectType
import com.crowdproj.kotlin.cor.handlers.CorChainDsl
import com.crowdproj.kotlin.cor.handlers.chain
import com.crowdproj.kotlin.cor.handlers.worker

fun CorChainDsl<CommentContext>.validateCommentFilter(title: String) = chain {
    this.title = title
    validateFilterObjectType("Validate comment filter object type")
    validateFilterNotEmpty("Validate comment filter not empty")
    validateFilterObjectIdProperFormat("Validate comment filter object id proper format")
    validateFilterUserIdProperFormat("Validate comment filter user proper format")
}

private fun CorChainDsl<CommentContext>.validateFilterObjectType(title: String) = worker {
    this.title = title
    on { commentFilterValidating.objectType == CommentObjectType.NONE }
    handle {
        fail(
            errorValidation(
                field = "filter-objectType",
                violationCode = "empty",
                description = "Unknown or empty"
            )
        )
    }
}

private fun CorChainDsl<CommentContext>.validateFilterNotEmpty(title: String) = worker {
    this.title = title
    on {
        commentFilterValidating.objectId.asString().isEmpty()
            && commentFilterValidating.userId.asString().isEmpty()
            && commentFilterValidating.objectType == CommentObjectType.NONE
    }
    handle {
        fail(
            errorValidation(
                field = "commentFilter",
                violationCode = "empty",
                description = "At least one id field must be set"
            )
        )
    }
}

private fun CorChainDsl<CommentContext>.validateFilterObjectIdProperFormat(title: String) = worker {
    this.title = title
    val regExp = Regex("^[0-9a-zA-Z-]*\$")
    on { !commentFilterValidating.objectId.asString().matches(regExp) }
    handle {
        val encodedId = commentFilterValidating.objectId.asString()
            .replace("<", "&lt;")
            .replace(">", "&gt;")

        fail(
            errorValidation(
                field = "filter-objectId",
                violationCode = "wrongFormat",
                description = "Value $encodedId must contain only letters and numbers",
            )
        )
    }
}

private fun CorChainDsl<CommentContext>.validateFilterUserIdProperFormat(title: String) = worker {
    this.title = title
    val regExp = Regex("^[0-9a-zA-Z-]*\$")
    on { !commentFilterValidating.userId.asString().matches(regExp) }
    handle {
        val encodedId = commentFilterValidating.userId.asString()
            .replace("<", "&lt;")
            .replace(">", "&gt;")

        fail(
            errorValidation(
                field = "filter-userId",
                violationCode = "wrongFormat",
                description = "Value $encodedId must contain only letters and numbers",
            )
        )
    }
}