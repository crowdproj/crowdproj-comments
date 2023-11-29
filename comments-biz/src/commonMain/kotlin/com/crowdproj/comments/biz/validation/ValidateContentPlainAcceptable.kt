package com.crowdproj.comments.biz.validation

import com.crowdproj.comments.common.CommentContext
import com.crowdproj.comments.common.helpers.errorValidation
import com.crowdproj.comments.common.helpers.fail
import com.crowdproj.comments.common.models.CommentContentType
import com.crowdproj.kotlin.cor.handlers.CorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker

fun CorChainDsl<CommentContext>.validateContentPlainAcceptable(title: String) = worker {
    this.title = title
    val regEpxPlain = Regex(".*[^-+ .,:!?_)(a-zA-Z0-9а-яА-Я]+.*")
    on { (commentValidating.contentType == CommentContentType.PLAIN && commentValidating.content.matches(regEpxPlain)) }
    handle {
        fail(
            errorValidation(
                field = "content",
                violationCode = "badCharacters",
                description = "Contains bad characters"
            )
        )
    }
}