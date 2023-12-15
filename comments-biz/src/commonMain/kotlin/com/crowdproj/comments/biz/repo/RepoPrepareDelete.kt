package com.crowdproj.comments.biz.repo

import com.crowdproj.comments.common.CommentContext
import com.crowdproj.comments.common.models.CommentState
import com.crowdproj.kotlin.cor.handlers.CorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker

fun CorChainDsl<CommentContext>.repoPrepareDelete(title: String) = worker {
    this.title = title
    description = "Prepare comment for delete"
    on { state == CommentState.RUNNING }
    handle {
        commentRepoPrepared = commentRepoRead.deepCopy().apply {
            lock = commentValidated.lock
        }
    }
}