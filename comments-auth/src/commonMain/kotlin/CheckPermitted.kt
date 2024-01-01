package com.crowdproj.comments.auth

import com.crowdproj.comments.common.models.CommentCommand
import com.crowdproj.comments.common.permissions.CommentsPrincipalRelations
import com.crowdproj.comments.common.permissions.CommentsUserPermissions

fun checkPermitted(
    command: CommentCommand,
    relations: Iterable<CommentsPrincipalRelations>,
    permissions: Iterable<CommentsUserPermissions>,
) =
    relations.asSequence().flatMap { relation ->
        permissions.map { permission ->
            AccessTableConditions(
                command = command,
                permission = permission,
                relation = relation,
            )
        }
    }.any {
        accessTable[it] != null
    }

private data class AccessTableConditions(
    val command: CommentCommand,
    val permission: CommentsUserPermissions,
    val relation: CommentsPrincipalRelations
)

private val accessTable = mapOf(
    // Create
    AccessTableConditions(
        command = CommentCommand.CREATE,
        permission = CommentsUserPermissions.CREATE_OWN,
        relation = CommentsPrincipalRelations.NEW,
    ) to true,

    // Read
    AccessTableConditions(
        command = CommentCommand.READ,
        permission = CommentsUserPermissions.READ_OWN,
        relation = CommentsPrincipalRelations.OWN,
    ) to true,
    AccessTableConditions(
        command = CommentCommand.READ,
        permission = CommentsUserPermissions.READ_PUBLIC,
        relation = CommentsPrincipalRelations.PUBLIC,
    ) to true,

    // Update
    AccessTableConditions(
        command = CommentCommand.UPDATE,
        permission = CommentsUserPermissions.UPDATE_OWN,
        relation = CommentsPrincipalRelations.OWN,
    ) to true,
    AccessTableConditions(
        command = CommentCommand.UPDATE,
        permission = CommentsUserPermissions.UPDATE_PUBLIC,
        relation = CommentsPrincipalRelations.PUBLIC,
    ) to true,

    // Delete
    AccessTableConditions(
        command = CommentCommand.DELETE,
        permission = CommentsUserPermissions.DELETE_OWN,
        relation = CommentsPrincipalRelations.OWN,
    ) to true,
    AccessTableConditions(
        command = CommentCommand.DELETE,
        permission = CommentsUserPermissions.DELETE_PUBLIC,
        relation = CommentsPrincipalRelations.PUBLIC,
    ) to true,
)