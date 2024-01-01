package com.crowdproj.comments.stubs

import com.crowdproj.comments.common.models.*

object CommentStubReview {
    val COMMENT_REVIEW: Comment
        get() = Comment(
            id = CommentId("5312"),
            objectType = CommentObjectType.PRODUCT,
            objectId = CommentObjectId("211"),
            userId = CommentUserId("2155"),
            content = "This is review of great product!",
            contentType = CommentContentType.PLAIN,
            createdAt = kotlinx.datetime.Instant.parse("2023-10-12T07:45:44Z"),
            updatedAt = kotlinx.datetime.Instant.parse("2023-10-13T21:23:12Z"),
            permissionsClient = mutableSetOf(
                CommentPermissionClient.READ,
                CommentPermissionClient.UPDATE,
                CommentPermissionClient.DELETE,
                CommentPermissionClient.MAKE_VISIBLE_PUBLIC,
                CommentPermissionClient.MAKE_VISIBLE_GROUP,
                CommentPermissionClient.MAKE_VISIBLE_OWNER
            )
        )
}