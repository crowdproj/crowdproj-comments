package com.crowdproj.comments.repo.stubs

import com.crowdproj.comments.common.repo.*
import com.crowdproj.comments.stubs.CommentsStub

class CommentsRepoStub : ICommentsRepository {
    override suspend fun createComment(request: DbCommentRequest): DbCommentResponse {
        return DbCommentResponse(
            data = CommentsStub.prepareResult {},
            isSuccess = true
        )
    }

    override suspend fun readComment(request: DbCommentIdRequest): DbCommentResponse {
        return DbCommentResponse(
            data = CommentsStub.prepareResult { },
            isSuccess = true
        )
    }

    override suspend fun updateComment(request: DbCommentRequest): DbCommentResponse {
        return DbCommentResponse(
            data = CommentsStub.prepareResult { },
            isSuccess = true
        )
    }

    override suspend fun deleteComment(request: DbCommentIdRequest): DbCommentResponse {
        return DbCommentResponse(
            data = CommentsStub.prepareResult { },
            isSuccess = true
        )
    }

    override suspend fun searchComments(request: DbCommentFilterRequest): DbCommentsResponse {
        return DbCommentsResponse(
            data = CommentsStub.prepareSearchList(),
            isSuccess = true
        )
    }
}