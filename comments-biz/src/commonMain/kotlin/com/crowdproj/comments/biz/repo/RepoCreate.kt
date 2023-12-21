package com.crowdproj.comments.biz.repo

import com.crowdproj.comments.common.CommentContext
import com.crowdproj.comments.common.helpers.fail
import com.crowdproj.comments.common.models.CommentState
import com.crowdproj.comments.common.repo.DbCommentRequest
import com.crowdproj.kotlin.cor.handlers.CorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker

fun CorChainDsl<CommentContext>.repoCreate(title: String) = worker {
    this.title = title
    description = "Adding comment to repoProd"
    on { state == CommentState.RUNNING }
    handle {
        val request = DbCommentRequest(commentRepoPrepared)
        val result = commentRepo.createComment(request)
        val resultComment = result.data
        if (result.isSuccess && resultComment != null) {
            commentRepoDone = resultComment
        } else
            fail(result.errors)
    }
}