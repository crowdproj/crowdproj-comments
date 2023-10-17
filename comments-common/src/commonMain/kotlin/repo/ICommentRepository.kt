package com.crowdproj.comments.common.repo

interface ICommentRepository {
    suspend fun createAd(rq: DbCommentRequest): DbCommentResponse
    suspend fun readAd(rq: DbCommentIdRequest): DbCommentResponse
    suspend fun updateAd(rq: DbCommentRequest): DbCommentResponse
    suspend fun deleteAd(rq: DbCommentIdRequest): DbCommentResponse
    suspend fun searchAd(rq: DbCommentFilterRequest): DbCommentsResponse
    companion object {
        val NONE = object : ICommentRepository {
            inner class NoneRepository : RuntimeException("Repository is not set")
            suspend fun forbidden(): Nothing = throw NoneRepository()
            override suspend fun createAd(rq: DbCommentRequest): DbCommentResponse {
                forbidden()
            }

            override suspend fun readAd(rq: DbCommentIdRequest): DbCommentResponse {
                forbidden()
            }

            override suspend fun updateAd(rq: DbCommentRequest): DbCommentResponse {
                forbidden()
            }

            override suspend fun deleteAd(rq: DbCommentIdRequest): DbCommentResponse {
                forbidden()
            }

            override suspend fun searchAd(rq: DbCommentFilterRequest): DbCommentsResponse {
                forbidden()
            }
        }
    }
}
