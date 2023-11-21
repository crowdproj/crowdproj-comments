package com.crowdproj.comments.common.repo

import com.crowdproj.comments.common.models.Comment
import com.crowdproj.comments.common.models.CommentId
import com.crowdproj.comments.common.models.CommentLock

data class DbCommentIdRequest(
    val id: CommentId,
    val lock: CommentLock = CommentLock.NONE,
) {
    constructor(comment: Comment): this(comment.id, comment.lock)
}
