package com.crowdproj.comments.auth

import com.crowdproj.comments.common.models.Comment
import com.crowdproj.comments.common.models.CommentId
import com.crowdproj.comments.common.permissions.CommentsPrincipalModel
import com.crowdproj.comments.common.permissions.CommentsPrincipalRelations

fun Comment.resolveRelationsTo(principal: CommentsPrincipalModel): Set<CommentsPrincipalRelations> = setOfNotNull(
    CommentsPrincipalRelations.NONE,
    CommentsPrincipalRelations.NEW.takeIf { id == CommentId.NONE },
    CommentsPrincipalRelations.OWN.takeIf { principal.id == userId },
    CommentsPrincipalRelations.PUBLIC
)
