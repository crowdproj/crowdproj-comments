package com.crowdproj.comments.biz.validation

import com.crowdproj.comments.common.CommentContext
import com.crowdproj.comments.common.helpers.errorValidation
import com.crowdproj.comments.common.helpers.fail
import com.crowdproj.kotlin.cor.handlers.CorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker


fun CorChainDsl<CommentContext>.validateCommentIdNotEmpty(title: String) = worker {
    this.title = title
    on { commentValidating.id.asString().isEmpty() }
    handle {
        fail(
            errorValidation(
                field = "id",
                violationCode = "empty",
                description = "field must be not empty",
            )
        )
    }
}

fun CorChainDsl<CommentContext>.validateObjectIdNotEmpty(title: String) = worker {
    this.title = title
    on { commentValidating.objectId.asString().isEmpty() }
    handle {
        fail(
            errorValidation(
                field = "objectId",
                violationCode = "empty",
                description = "field must be not empty",
            )
        )
    }
}

fun CorChainDsl<CommentContext>.validateUserIdNotEmpty(title: String) = worker {
    this.title = title
    on { commentValidating.userId.asString().isEmpty() }
    handle {
        fail(
            errorValidation(
                field = "userId",
                violationCode = "empty",
                description = "field must be not empty",
            )
        )
    }
}

fun CorChainDsl<CommentContext>.validateCommentIdEmpty(title: String) = worker {
    this.title = title
    on { commentValidating.id.asString().isNotEmpty() }
    handle {
        fail(
            errorValidation(
                field = "id",
                violationCode = "empty",
                description = "field must empty",
            )
        )
    }
}