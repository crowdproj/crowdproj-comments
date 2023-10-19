package com.crowdproj.comments.common.config

import com.crowdproj.comments.common.repo.ICommentRepository

data class CommentsCorSettings(
    val repoTest: ICommentRepository = ICommentRepository.NONE,
    val repoStub: ICommentRepository = ICommentRepository.NONE,
    val repoProd: ICommentRepository = ICommentRepository.NONE,
) {
    companion object {
        val NONE = CommentsCorSettings()
    }
}