package com.crowdproj.comments.common.helpers

import com.crowdproj.comments.common.CommentContext
import com.crowdproj.comments.common.models.CommentCommand

fun CommentContext.isUpdatableCommand() = command in listOf(CommentCommand.CREATE, CommentCommand.UPDATE, CommentCommand.DELETE)