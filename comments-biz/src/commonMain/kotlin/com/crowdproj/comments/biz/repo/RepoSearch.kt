package com.crowdproj.comments.biz.repo

import com.crowdproj.comments.common.CommentContext
import com.crowdproj.comments.common.helpers.fail
import com.crowdproj.comments.common.models.CommentState
import com.crowdproj.comments.common.repo.DbCommentFilterRequest
import com.crowdproj.kotlin.cor.handlers.CorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker

fun CorChainDsl<CommentContext>.repoSearch(title: String) = worker {
    this.title = title
    description = "Search comment in repoProd by filters"
    on { state == CommentState.RUNNING }
    handle {
        val request = DbCommentFilterRequest(
            objectType = commentFilterValidated.objectType,
            objectId = commentFilterValidated.objectId,
            userId = commentFilterValidated.userId,
        )
        val result = commentRepo.searchComments(request)
        val resultComments = result.data
        if (result.isSuccess && resultComments != null) {
            commentsRepoDone = resultComments.toMutableList()
        } else fail(result.errors)
    }
}