package com.crowdproj.comments.biz.workers

import com.crowdproj.comments.common.CommentContext
import com.crowdproj.comments.common.models.CommentState
import com.crowdproj.kotlin.cor.handlers.CorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker

fun CorChainDsl<CommentContext>.initChain(title: String) = worker{
    this.title = title
    on { state == CommentState.NONE }
    handle {
        state = CommentState.RUNNING
    }
}