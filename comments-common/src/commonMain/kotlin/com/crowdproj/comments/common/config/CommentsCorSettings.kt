package com.crowdproj.comments.common.config

import com.crowdproj.comments.common.repo.ICommentsRepository
import com.crowdproj.comments.logging.common.CommentsLoggerProvider

data class CommentsCorSettings(
    val repoProd: ICommentsRepository = ICommentsRepository.NONE,
    val repoTest: ICommentsRepository = ICommentsRepository.NONE,
    val repoStub: ICommentsRepository = ICommentsRepository.NONE,
    val loggerProvider: CommentsLoggerProvider = CommentsLoggerProvider(),
) {
    companion object {
        val NONE = CommentsCorSettings()
    }
}