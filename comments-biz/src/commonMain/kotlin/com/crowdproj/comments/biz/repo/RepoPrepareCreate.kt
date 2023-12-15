package com.crowdproj.comments.biz.repo

import com.crowdproj.comments.common.CommentContext
import com.crowdproj.comments.common.models.CommentState
import com.crowdproj.kotlin.cor.handlers.CorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker

fun CorChainDsl<CommentContext>.repoPrepareCreate(title: String) = worker {
    this.title = title
    description = "Prepare object to save in repoProd"
    on { state == CommentState.RUNNING }
    handle {
        commentRepoRead = commentValidated.deepCopy()
        commentRepoPrepared = commentRepoRead
    }
}