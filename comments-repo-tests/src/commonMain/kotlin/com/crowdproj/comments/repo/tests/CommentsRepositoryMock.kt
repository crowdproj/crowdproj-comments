package com.crowdproj.comments.repo.tests

import com.crowdproj.comments.common.repo.*

class CommentsRepositoryMock(
    private val invokeCreateComment: (DbCommentRequest) -> DbCommentResponse = { DbCommentResponse.MOCK_SUCCESS_EMPTY },
    private val invokeReadComment: (DbCommentIdRequest) -> DbCommentResponse = { DbCommentResponse.MOCK_SUCCESS_EMPTY },
    private val invokeUpdateComment: (DbCommentRequest) -> DbCommentResponse = { DbCommentResponse.MOCK_SUCCESS_EMPTY },
    private val invokeDeleteComment: (DbCommentIdRequest) -> DbCommentResponse = { DbCommentResponse.MOCK_SUCCESS_EMPTY },
    private val invokeSearchComment: (DbCommentFilterRequest) -> DbCommentsResponse = { DbCommentsResponse.MOCK_SUCCESS_EMPTY },
) : ICommentsRepository {
    override suspend fun createComment(request: DbCommentRequest): DbCommentResponse {
        return invokeCreateComment(request)
    }

    override suspend fun readComment(request: DbCommentIdRequest): DbCommentResponse {
        return invokeReadComment(request)
    }

    override suspend fun updateComment(request: DbCommentRequest): DbCommentResponse {
        return invokeUpdateComment(request)
    }

    override suspend fun deleteComment(request: DbCommentIdRequest): DbCommentResponse {
        return invokeDeleteComment(request)
    }

    override suspend fun searchComments(request: DbCommentFilterRequest): DbCommentsResponse {
        return invokeSearchComment(request)
    }
}