package com.crowdproj.comments.stubs

import com.crowdproj.comments.common.models.*
import com.crowdproj.comments.stubs.CommentStubReview.COMMENT_REVIEW

object CommentStub {
    fun get(): Comment = COMMENT_REVIEW.copy()

    fun prepareResult(block: Comment.() -> Unit): Comment = get().apply(block)

    fun prepareSearchList(filter: String, objectType: CommentObjectType) = listOf(
        commentReview("cr-111-01", filter, objectType),
        commentReview("cr-111-02", filter, objectType),
        commentReview("cr-111-03", filter, objectType),
        commentReview("cr-111-04", filter, objectType),
        commentReview("cr-111-05", filter, objectType),
        commentReview("cr-111-06", filter, objectType),
    )

    private fun commentReview(id: String, filter: String, objectType: CommentObjectType) =
        comment(COMMENT_REVIEW, id, filter, objectType)

    private fun comment(base: Comment, id: String, objectId: String, objectType: CommentObjectType) = base.copy(
        id = CommentId(id),
        objectId = CommentObjectId(objectId),
        objectType = objectType
    )
}