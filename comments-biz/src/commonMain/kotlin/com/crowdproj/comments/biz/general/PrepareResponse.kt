package com.crowdproj.comments.biz.general

import com.crowdproj.comments.common.CommentContext
import com.crowdproj.comments.common.models.CommentState
import com.crowdproj.comments.common.models.CommentWorkMode
import com.crowdproj.kotlin.cor.handlers.CorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker

fun CorChainDsl<CommentContext>.prepareResponse(title: String) = worker{
    this.title = title
    description = "Prepare data for response on client request"
    on { workMode != CommentWorkMode.STUB }
    handle {
        commentResponse = commentRepoDone
        commentsResponse = commentsRepoDone
        state = when (val st = state) {
            CommentState.RUNNING -> CommentState.FINISHING
            else -> st
        }
    }
}