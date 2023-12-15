package com.crowdproj.comments.repo.tests

import com.crowdproj.comments.common.models.*
import kotlinx.datetime.Instant

abstract class BaseInitComments(val operation: String): IInitObjects {
    open val lockOld: CommentLock = CommentLock("20000000-0000-0000-0000-000000000001")
    fun createInitTestModel(
        suffix: String,
        objectType: CommentObjectType = CommentObjectType.AD,
        objectId: CommentObjectId = CommentObjectId("object-123"),
        userId: CommentUserId = CommentUserId("user-123"),
        contentType: CommentContentType = CommentContentType.PLAIN,
        createdAt: Instant = Instant.parse("2023-01-01T00:00:00Z"),
        updatedAt: Instant = createdAt,
        lock: CommentLock = lockOld
    ) = Comment(
        id = CommentId("comment-repoProd-$operation-$suffix"),
        objectType = objectType,
        objectId = objectId,
        userId = userId,
        content = "$suffix stub comment",
        contentType = contentType,
        createdAt = createdAt,
        updatedAt = updatedAt,
        lock = lock
    )
}