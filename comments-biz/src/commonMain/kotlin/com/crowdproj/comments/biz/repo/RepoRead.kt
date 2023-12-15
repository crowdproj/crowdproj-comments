package com.crowdproj.comments.biz.repo

import com.crowdproj.comments.common.CommentContext
import com.crowdproj.comments.common.helpers.fail
import com.crowdproj.comments.common.models.CommentState
import com.crowdproj.comments.common.repo.DbCommentIdRequest
import com.crowdproj.kotlin.cor.handlers.CorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker

fun CorChainDsl<CommentContext>.repoRead(title: String) = worker {
    this.title = title
    description = "Reading comment from repoProd"
    on { state == CommentState.RUNNING }
    handle {
        val request = DbCommentIdRequest(commentValidated)
        val result = commentRepo.readComment(request)
        val resultComment = result.data
        if (result.isSuccess && resultComment != null) {
            commentRepoRead = resultComment
        } else
            fail(result.errors)
    }
}