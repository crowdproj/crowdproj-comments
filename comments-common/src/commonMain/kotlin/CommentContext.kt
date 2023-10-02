package com.crowdproj.comments.common

import com.crowdproj.comments.common.models.*
import com.crowdproj.comments.common.stubs.CommentStubs
import kotlinx.datetime.Instant

data class CommentContext (
    var command: CommentCommand = CommentCommand.NONE,
    var state: CommentState = CommentState.NONE,
    var error: MutableList<CommentError> = mutableListOf(),

    var workMode: CommentWorkMode = CommentWorkMode.TEST,
    var stubCase: CommentStubs = CommentStubs.NONE,

    var requestId: CommentRequestId = CommentRequestId.NONE,
    var timeStart: Instant = Instant.DISTANT_PAST,

    var commentRequest: Comment = Comment.NONE,
    var commentsRequest: MutableList<Comment> = mutableListOf(),
    var commentFilterRequest: CommentFilter = CommentFilter.NONE,

    var commentResponse: Comment = Comment.NONE,
    var commentsResponse: MutableList<Comment> = mutableListOf(),


    )