package com.crowdproj.comments.biz.repo

import com.crowdproj.comments.common.CommentContext
import com.crowdproj.comments.common.models.CommentState
import com.crowdproj.kotlin.cor.handlers.CorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker

fun CorChainDsl<CommentContext>.repoReadDone(title: String) = worker {
    this.title = title
    description = "Finishing reading comment from repoProd"
    on { state == CommentState.RUNNING }
    handle {
        commentRepoDone = commentRepoRead
    }
}