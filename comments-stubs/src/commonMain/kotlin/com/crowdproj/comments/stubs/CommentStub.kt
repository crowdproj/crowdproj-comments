package com.crowdproj.comments.stubs

import com.crowdproj.comments.common.models.*
import com.crowdproj.comments.stubs.CommentStubReview.COMMENT_REVIEW

object CommentStub {
    fun get(): Comment = COMMENT_REVIEW.copy()

    fun prepareResult(block: Comment.() -> Unit): Comment = get().apply(block)

    fun prepareSearchList(filterObjectType: CommentObjectType = CommentObjectType.NONE, filterObjectId: String? = null, filterUserId: String? = null) = listOf(
        commentReview("cr-111-01", filterObjectType, filterObjectId, filterUserId),
        commentReview("cr-111-02", filterObjectType, filterObjectId, filterUserId),
        commentReview("cr-111-03", filterObjectType, filterObjectId, filterUserId),
        commentReview("cr-111-04", filterObjectType, filterObjectId, filterUserId),
        commentReview("cr-111-05", filterObjectType, filterObjectId, filterUserId),
        commentReview("cr-111-06", filterObjectType, filterObjectId, filterUserId),
    )

    private fun commentReview(id: String, filterObjectType: CommentObjectType, filterObjectId: String?, filterUserId: String?) =
        comment(COMMENT_REVIEW, id, filterObjectId, filterObjectType, filterUserId)

    private fun comment(base: Comment, id: String, objectId: String?, objectType: CommentObjectType, userId: String?) = base.copy(
        id = CommentId(id),
        objectId = objectId?.let { CommentObjectId(it) } ?: base.objectId,
        objectType = objectType.takeIf { it != CommentObjectType.NONE } ?: base.objectType,
        userId = userId?.let { CommentUserId(it) } ?: base.userId
    )
}