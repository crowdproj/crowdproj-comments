package com.crowdproj.comments.common.repo

import com.crowdproj.comments.common.models.CommentError

interface IDbResponse<T> {
    val data: T?
    val isSuccess: Boolean
    val errors: List<CommentError>
}
