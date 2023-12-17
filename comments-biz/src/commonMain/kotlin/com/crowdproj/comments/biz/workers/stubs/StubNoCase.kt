package com.crowdproj.comments.biz.workers.stubs

import com.crowdproj.comments.common.CommentContext
import com.crowdproj.comments.common.helpers.fail
import com.crowdproj.comments.common.models.CommentError
import com.crowdproj.comments.common.models.CommentState
import com.crowdproj.kotlin.cor.handlers.CorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker

fun CorChainDsl<CommentContext>.stubNoCase(title: String) = worker {
    this.title = title
    on { state == CommentState.RUNNING }
    handle {
        fail(
            CommentError(
                code = "validation",
                field = "stub",
                group = CommentError.Group.VALIDATION,
                message = "Wrong stub case. Given stub: ${stubCase.name}"
            )
        )
    }

}