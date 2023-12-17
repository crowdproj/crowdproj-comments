package com.crowdproj.comments.common.config

import com.crowdproj.comments.common.repo.ICommentRepository
import com.crowdproj.comments.logging.common.CommentsLoggerProvider

data class CommentsCorSettings(
    val repo: ICommentRepository = ICommentRepository.NONE,
    val loggerProvider: CommentsLoggerProvider = CommentsLoggerProvider()
) {
    companion object {
        val NONE = CommentsCorSettings()
    }
}