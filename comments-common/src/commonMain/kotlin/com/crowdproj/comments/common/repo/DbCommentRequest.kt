package com.crowdproj.comments.common.repo

import com.crowdproj.comments.common.models.Comment


data class DbCommentRequest(
    val comment: Comment
)
