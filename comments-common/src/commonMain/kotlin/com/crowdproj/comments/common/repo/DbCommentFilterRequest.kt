package com.crowdproj.comments.common.repo

import com.crowdproj.comments.common.models.CommentObjectId
import com.crowdproj.comments.common.models.CommentObjectType
import com.crowdproj.comments.common.models.CommentUserId

data class DbCommentFilterRequest(
    val objectType: CommentObjectType = CommentObjectType.NONE,
    val objectId: CommentObjectId = CommentObjectId.NONE,
    val userId: CommentUserId = CommentUserId.NONE,
)
