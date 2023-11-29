package com.crowdproj.comments.common

import com.crowdproj.comments.common.models.*
import com.crowdproj.comments.common.stubs.CommentStubs
import kotlinx.datetime.Instant

data class CommentContext(
    var command: CommentCommand = CommentCommand.NONE,
    var state: CommentState = CommentState.NONE,
    val errors: MutableList<CommentError> = mutableListOf(),

    var workMode: CommentWorkMode = CommentWorkMode.TEST,
    var stubCase: CommentStubs = CommentStubs.NONE,

    var requestId: CommentRequestId = CommentRequestId.NONE,
    var timeStarted: Instant = Instant.DISTANT_PAST,

    var commentRequest: Comment = Comment.NONE,
    val commentsRequest: MutableList<Comment> = mutableListOf(),
    var commentFilterRequest: CommentFilter = CommentFilter.NONE,

    var commentValidating: Comment = Comment(),
    var commentFilterValidating: CommentFilter = CommentFilter(),

    var commentValidated: Comment = Comment(),
    var commentFilterValidated: CommentFilter = CommentFilter(),

    var commentResponse: Comment = Comment.NONE,
    val commentsResponse: MutableList<Comment> = mutableListOf()
)