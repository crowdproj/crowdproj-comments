package com.crowdproj.comments.common.repo

interface ICommentRepository {
    suspend fun createComment(rq: DbCommentRequest): DbCommentResponse
    suspend fun readComment(rq: DbCommentIdRequest): DbCommentResponse
    suspend fun updateComment(rq: DbCommentRequest): DbCommentResponse
    suspend fun deleteComment(rq: DbCommentIdRequest): DbCommentResponse
    suspend fun searchComment(rq: DbCommentFilterRequest): DbCommentsResponse
    companion object {
        val NONE = object : ICommentRepository {
            inner class NoneRepository : RuntimeException("Repository is not set")
            fun forbidden(): Nothing = throw NoneRepository()
            override suspend fun createComment(rq: DbCommentRequest): DbCommentResponse {
                forbidden()
            }

            override suspend fun readComment(rq: DbCommentIdRequest): DbCommentResponse {
                forbidden()
            }

            override suspend fun updateComment(rq: DbCommentRequest): DbCommentResponse {
                forbidden()
            }

            override suspend fun deleteComment(rq: DbCommentIdRequest): DbCommentResponse {
                forbidden()
            }

            override suspend fun searchComment(rq: DbCommentFilterRequest): DbCommentsResponse {
                forbidden()
            }
        }
    }
}
