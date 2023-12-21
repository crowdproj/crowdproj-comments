package com.crowdproj.comments.biz.repo

import com.crowdproj.comments.common.CommentContext
import com.crowdproj.comments.common.models.CommentState
import com.crowdproj.kotlin.cor.handlers.CorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker

fun CorChainDsl<CommentContext>.repoPrepareUpdate(title: String) = worker {
    this.title = title
    description = "Prepare comment for update: joining data from repoProd and request from user"
    on { state == CommentState.RUNNING }
    handle {
        commentRepoPrepared = commentRepoRead.deepCopy().apply {
            objectType = commentValidated.objectType
            objectId = commentValidated.objectId
            userId = commentValidated.userId
            content = commentValidated.content
            contentType = commentValidated.contentType
            lock = commentValidated.lock
        }
    }
}