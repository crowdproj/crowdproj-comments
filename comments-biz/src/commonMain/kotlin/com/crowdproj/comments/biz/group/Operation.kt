package com.crowdproj.comments.biz.group

import com.crowdproj.comments.common.CommentContext
import com.crowdproj.comments.common.models.CommentCommand
import com.crowdproj.comments.common.models.CommentState
import com.crowdproj.kotlin.cor.handlers.CorChainDsl
import com.crowdproj.kotlin.cor.handlers.chain

fun CorChainDsl<CommentContext>.operation(title: String, command: CommentCommand, block: CorChainDsl<CommentContext>.() -> Unit) = chain {
    block()
    this.title = title
    on { this.command == command && state == CommentState.RUNNING }
}