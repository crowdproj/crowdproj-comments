package com.crowdproj.comments.common.repo

interface ICommentsRepository {
    suspend fun createComment(request: DbCommentRequest): DbCommentResponse
    suspend fun readComment(request: DbCommentIdRequest): DbCommentResponse
    suspend fun updateComment(request: DbCommentRequest): DbCommentResponse
    suspend fun deleteComment(request: DbCommentIdRequest): DbCommentResponse
    suspend fun searchComments(request: DbCommentFilterRequest): DbCommentsResponse
    companion object {
        val NONE = object : ICommentsRepository {
            inner class NoneRepository : RuntimeException("Repository is not set")
            fun forbidden(): Nothing = throw NoneRepository()
            override suspend fun createComment(request: DbCommentRequest): DbCommentResponse {
                forbidden()
            }

            override suspend fun readComment(request: DbCommentIdRequest): DbCommentResponse {
                forbidden()
            }

            override suspend fun updateComment(request: DbCommentRequest): DbCommentResponse {
                forbidden()
            }

            override suspend fun deleteComment(request: DbCommentIdRequest): DbCommentResponse {
                forbidden()
            }

            override suspend fun searchComments(request: DbCommentFilterRequest): DbCommentsResponse {
                forbidden()
            }
        }
    }
}
