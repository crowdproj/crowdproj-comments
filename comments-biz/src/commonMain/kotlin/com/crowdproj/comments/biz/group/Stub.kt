package com.crowdproj.comments.biz.group

import com.crowdproj.comments.common.CommentContext
import com.crowdproj.comments.common.models.CommentState
import com.crowdproj.comments.common.models.CommentWorkMode
import com.crowdproj.kotlin.cor.handlers.CorChainDsl
import com.crowdproj.kotlin.cor.handlers.chain

fun CorChainDsl<CommentContext>.stub(title: String, block: CorChainDsl<CommentContext>.() -> Unit) = chain {
    block()
    this.title = title
    on { workMode == CommentWorkMode.STUB && state == CommentState.RUNNING }
}