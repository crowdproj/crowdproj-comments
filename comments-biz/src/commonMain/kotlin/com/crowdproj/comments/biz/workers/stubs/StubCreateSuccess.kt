package com.crowdproj.comments.biz.workers.stubs

import com.crowdproj.comments.common.CommentContext
import com.crowdproj.comments.common.models.*
import com.crowdproj.comments.common.stubs.CommentStubs
import com.crowdproj.comments.stubs.CommentStub
import com.crowdproj.kotlin.cor.handlers.CorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker

fun CorChainDsl<CommentContext>.stubCreateSuccess(title: String) = worker {
    this.title = title
    on { state == CommentState.RUNNING && stubCase == CommentStubs.SUCCESS }
    handle {
        val stub = CommentStub.prepareResult {
            state = CommentState.FINISHING
            commentRequest.content.takeIf { it.isNotBlank() }?.also { this.content = it }
            commentRequest.contentType.takeIf { it != CommentContentType.NONE }?.also { this.contentType = it }
            commentRequest.objectId.takeIf { it != CommentObjectId.NONE }?.also { this.objectId = it }
            commentRequest.objectType.takeIf { it != CommentObjectType.NONE }?.also { this.objectType = it }
            commentRequest.userId.takeIf { it != CommentUserId.NONE }?.also { this.userId = it }
        }
        commentResponse = stub
    }
}