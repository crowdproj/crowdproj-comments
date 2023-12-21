package com.crowdproj.comments.biz.repo

import com.crowdproj.comments.common.CommentContext
import com.crowdproj.comments.common.models.CommentState
import com.crowdproj.kotlin.cor.handlers.CorChainDsl

fun CorChainDsl<CommentContext>.repository(title: String, block: CorChainDsl<CommentContext>.() -> Unit) {
    block()
    this.title = title
    on { state == CommentState.RUNNING }
}