package com.crowdproj.comments.biz.workers.stubs

import com.crowdproj.comments.common.CommentContext
import com.crowdproj.comments.common.models.CommentObjectId
import com.crowdproj.comments.common.models.CommentState
import com.crowdproj.comments.common.models.CommentUserId
import com.crowdproj.comments.common.stubs.CommentStubs
import com.crowdproj.comments.stubs.CommentsStub
import com.crowdproj.kotlin.cor.handlers.CorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker

fun CorChainDsl<CommentContext>.stubSearchSuccess(title: String) = worker {
    this.title = title
    on { state == CommentState.RUNNING && stubCase == CommentStubs.SUCCESS }
    handle {
        state = CommentState.FINISHING
        val stub = CommentsStub.prepareResult {
            commentsResponse.addAll(
                CommentsStub.prepareSearchList(
                    commentFilterRequest.objectType,
                    commentFilterRequest.objectId.takeIf { it != CommentObjectId.NONE }?.asString(),
                    commentFilterRequest.userId.takeIf { it != CommentUserId.NONE }?.asString())
            )
        }
        commentResponse = stub
    }
}