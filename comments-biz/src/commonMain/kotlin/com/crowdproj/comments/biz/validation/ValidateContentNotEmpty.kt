package com.crowdproj.comments.biz.validation

import com.crowdproj.comments.common.CommentContext
import com.crowdproj.comments.common.helpers.errorValidation
import com.crowdproj.comments.common.helpers.fail
import com.crowdproj.kotlin.cor.handlers.CorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker

fun CorChainDsl<CommentContext>.validateContentNotEmpty(title: String) = worker {
    this.title = title
    on { commentValidating.content.isEmpty() }
    handle {
        fail(
            errorValidation(
                field = "content",
                violationCode = "empty",
                description = "field must be not empty"
            )
        )
    }
}