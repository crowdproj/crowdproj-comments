package com.crowdproj.comments.biz.workers.stubs

import com.crowdproj.comments.common.CommentContext
import com.crowdproj.comments.common.models.*
import com.crowdproj.comments.common.stubs.CommentStubs
import com.crowdproj.comments.stubs.CommentStub
import com.crowdproj.kotlin.cor.handlers.CorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker

fun CorChainDsl<CommentContext>.stubReadSuccess(title: String) = worker {
    this.title = title
    on { state == CommentState.RUNNING && stubCase == CommentStubs.SUCCESS }
    handle {
        state = CommentState.FINISHING
        val stub = CommentStub.prepareResult {
            commentRequest.id.takeIf { it != CommentId.NONE }?.also { this.id = it }
        }
        commentResponse = stub
    }
}