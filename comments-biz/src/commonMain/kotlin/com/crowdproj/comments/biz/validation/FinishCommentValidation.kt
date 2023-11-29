package com.crowdproj.comments.biz.validation

import com.crowdproj.comments.common.CommentContext
import com.crowdproj.comments.common.models.CommentState
import com.crowdproj.kotlin.cor.handlers.CorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker

fun CorChainDsl<CommentContext>.finishCommentValidation(title: String) = worker {
    this.title = title
    on { state == CommentState.RUNNING }
    handle {
        commentValidated = commentValidating
    }
}

fun CorChainDsl<CommentContext>.finishCommentFilterValidation(title: String) = worker {
    this.title = title
    on { state == CommentState.RUNNING }
    handle {
        commentFilterValidated = commentFilterValidating
    }
}