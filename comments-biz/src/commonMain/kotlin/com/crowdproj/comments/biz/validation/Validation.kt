package com.crowdproj.comments.biz.validation

import com.crowdproj.comments.common.CommentContext
import com.crowdproj.comments.common.models.CommentState
import com.crowdproj.kotlin.cor.handlers.CorChainDsl
import com.crowdproj.kotlin.cor.handlers.chain

fun CorChainDsl<CommentContext>.validation(block: CorChainDsl<CommentContext>.() -> Unit) = chain {
    block()
    title = "Validation"
    on { state == CommentState.RUNNING }
}