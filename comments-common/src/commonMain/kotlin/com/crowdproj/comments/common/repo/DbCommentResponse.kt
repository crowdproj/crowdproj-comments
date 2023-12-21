package com.crowdproj.comments.common.repo

import com.crowdproj.comments.common.models.Comment
import com.crowdproj.comments.common.models.CommentError


data class DbCommentResponse(
    override val data: Comment?,
    override val isSuccess: Boolean,
    override val errors: List<CommentError> = emptyList()
): IDbResponse<Comment> {

    companion object {
        val MOCK_SUCCESS_EMPTY = DbCommentResponse(null, true)
        fun success(result: Comment) = DbCommentResponse(result, true)
        fun error(errors: List<CommentError>) = DbCommentResponse(null, false, errors)
        fun error(error: CommentError) = DbCommentResponse(null, false, listOf(error))
        fun error(data: Comment?, error: CommentError) = DbCommentResponse(data, false, listOf(error))
        fun error(data: Comment?, errors: List<CommentError>) = DbCommentResponse(data, false, errors)
    }
}
