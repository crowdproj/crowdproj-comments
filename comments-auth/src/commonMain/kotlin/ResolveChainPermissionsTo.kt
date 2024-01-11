package com.crowdproj.comments.auth

import com.crowdproj.comments.common.permissions.CommentsUserGroups
import com.crowdproj.comments.common.permissions.CommentsUserPermissions

fun resolveChainPermissions(
    groups: Iterable<CommentsUserGroups>,
) = mutableSetOf<CommentsUserPermissions>()
    .apply {
        addAll(groups.flatMap { groupPermissionsAdmits[it] ?: emptySet() })
        removeAll(groups.flatMap { groupPermissionsDenys[it] ?: emptySet() }.toSet())
    }
    .toSet()

private val groupPermissionsAdmits = mapOf(
    CommentsUserGroups.USER to setOf(
        CommentsUserPermissions.READ_OWN,
        CommentsUserPermissions.READ_PUBLIC,
        CommentsUserPermissions.CREATE_OWN,
        CommentsUserPermissions.UPDATE_OWN,
        CommentsUserPermissions.DELETE_OWN,
    ),
    CommentsUserGroups.MODERATOR to setOf(
        CommentsUserPermissions.UPDATE_PUBLIC,
        CommentsUserPermissions.DELETE_PUBLIC
    ),
    CommentsUserGroups.ADMIN_CWP to setOf(),
    CommentsUserGroups.TEST to setOf(),
    CommentsUserGroups.BANNED to setOf(),
)

private val groupPermissionsDenys = mapOf(
    CommentsUserGroups.USER to setOf(),
    CommentsUserGroups.MODERATOR to setOf(),
    CommentsUserGroups.ADMIN_CWP to setOf(),
    CommentsUserGroups.TEST to setOf(),
    CommentsUserGroups.BANNED to setOf(
        CommentsUserPermissions.UPDATE_OWN,
        CommentsUserPermissions.CREATE_OWN,
    ),
)