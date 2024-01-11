package com.crowdproj.comments.common.permissions

import com.crowdproj.comments.common.models.CommentUserId

data class CommentsPrincipalModel(
    val id: CommentUserId = CommentUserId.NONE,
    val fname: String = "",
    val mname: String = "",
    val lname: String = "",
    val groups: Set<CommentsUserGroups> = emptySet()
) {
    companion object {
        val NONE = CommentsPrincipalModel()
    }
}