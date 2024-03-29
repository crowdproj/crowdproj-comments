package com.crowdproj.comments.common.models

import com.crowdproj.comments.common.NONE
import com.crowdproj.comments.common.permissions.CommentsPrincipalRelations
import kotlinx.datetime.Instant

data class Comment(
    var id: CommentId = CommentId.NONE,
    var objectType: CommentObjectType = CommentObjectType.NONE,
    var objectId: CommentObjectId = CommentObjectId.NONE,
    var userId: CommentUserId = CommentUserId.NONE,
    var content: String = "",
    var contentType: CommentContentType = CommentContentType.NONE,
    var createdAt: Instant = Instant.NONE,
    var updatedAt: Instant = Instant.NONE,
    var lock: CommentLock = CommentLock.NONE,
    var principalRelations: Set<CommentsPrincipalRelations> = emptySet(),
    var permissionsClient: MutableSet<CommentPermissionClient> = mutableSetOf(),
) {
    fun deepCopy(): Comment = copy(
        permissionsClient = permissionsClient.toMutableSet()
    )

    companion object {
        val NONE = Comment()
    }
}