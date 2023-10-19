package com.crowdproj.comments.common.repo

import com.crowdproj.comments.common.models.Comment
import com.crowdproj.comments.common.models.CommentError

data class DbCommentsResponse(
    override val data: List<Comment>?,
    override val isSuccess: Boolean,
    override val errors: List<CommentError> = emptyList(),
): IDbResponse<List<Comment>> {

    companion object {
        val MOCK_SUCCESS_EMPTY = DbCommentsResponse(emptyList(), true)
        fun success(result: List<Comment>) = DbCommentsResponse(result, true)
        fun error(errors: List<CommentError>) = DbCommentsResponse(null, false, errors)
        fun error(error: CommentError) = DbCommentsResponse(null, false, listOf(error))
    }
}
