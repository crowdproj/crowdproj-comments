package com.crowdproj.comments.biz.repo

import com.crowdproj.comments.common.CommentContext
import com.crowdproj.comments.common.helpers.fail
import com.crowdproj.comments.common.models.CommentState
import com.crowdproj.comments.common.repo.DbCommentIdRequest
import com.crowdproj.kotlin.cor.handlers.CorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker

fun CorChainDsl<CommentContext>.repoDelete(title: String) = worker {
    this.title = title
    description = "Delete comment from repoProd"
    on { state == CommentState.RUNNING }
    handle {
        val request = DbCommentIdRequest(commentRepoPrepared)
        val result = commentRepo.deleteComment(request)
        if(!result.isSuccess)
            fail(result.errors)
        commentRepoDone = commentRepoRead
    }

}