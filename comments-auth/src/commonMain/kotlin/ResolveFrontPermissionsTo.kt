package com.crowdproj.comments.auth

import com.crowdproj.comments.common.models.CommentPermissionClient
import com.crowdproj.comments.common.permissions.CommentsPrincipalRelations
import com.crowdproj.comments.common.permissions.CommentsUserPermissions

fun resolveFrontPermissions(
    permissions: Iterable<CommentsUserPermissions>,
    relations: Iterable<CommentsPrincipalRelations>,
) = mutableSetOf<CommentPermissionClient>()
    .apply {
        for (permission in permissions) {
            for (relation in relations) {
                accessTable[permission]?.get(relation)?.let { this@apply.add(it) }
            }
        }
    }
    .toSet()

private val accessTable = mapOf(
    // READ
    CommentsUserPermissions.READ_OWN to mapOf(
        CommentsPrincipalRelations.OWN to CommentPermissionClient.READ
    ),
    CommentsUserPermissions.READ_PUBLIC to mapOf(
        CommentsPrincipalRelations.PUBLIC to CommentPermissionClient.READ
    ),

    // UPDATE
    CommentsUserPermissions.UPDATE_OWN to mapOf(
        CommentsPrincipalRelations.OWN to CommentPermissionClient.UPDATE
    ),
    CommentsUserPermissions.UPDATE_PUBLIC to mapOf(
        CommentsPrincipalRelations.PUBLIC to CommentPermissionClient.UPDATE
    ),

    // DELETE
    CommentsUserPermissions.DELETE_OWN to mapOf(
        CommentsPrincipalRelations.OWN to CommentPermissionClient.DELETE
    ),
    CommentsUserPermissions.DELETE_PUBLIC to mapOf(
        CommentsPrincipalRelations.PUBLIC to CommentPermissionClient.DELETE
    ),
)
