package com.crowdproj.comments.biz.validation

import com.crowdproj.comments.common.CommentContext
import com.crowdproj.comments.common.helpers.errorValidation
import com.crowdproj.comments.common.helpers.fail
import com.crowdproj.comments.common.models.CommentContentType
import com.crowdproj.kotlin.cor.handlers.CorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker

fun CorChainDsl<CommentContext>.validateContentType(title: String) = worker {
    this.title = title
    on { commentValidating.contentType == CommentContentType.NONE }
    handle {
        fail(
            errorValidation(
                field = "contentType",
                violationCode = "type",
                description = "Unknown or empty"
            )
        )
    }

}