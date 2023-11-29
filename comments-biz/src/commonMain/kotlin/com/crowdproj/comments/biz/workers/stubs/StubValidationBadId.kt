package com.crowdproj.comments.biz.workers.stubs

import com.crowdproj.comments.common.CommentContext
import com.crowdproj.comments.common.models.CommentError
import com.crowdproj.comments.common.models.CommentState
import com.crowdproj.comments.common.stubs.CommentStubs
import com.crowdproj.kotlin.cor.handlers.CorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker

fun CorChainDsl<CommentContext>.stubValidationBadId(title: String) = worker {
    this.title = title
    on { state == CommentState.RUNNING && stubCase == CommentStubs.BAD_ID }
    handle {
        state = CommentState.FAILING
        this.errors.add(
            CommentError(
                group = CommentError.Group.VALIDATION,
                code = "validation-id",
                message = "Bad comment id",
                field = "id"
            )
        )
    }
}