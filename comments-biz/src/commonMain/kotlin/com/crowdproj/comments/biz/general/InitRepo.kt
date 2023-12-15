package com.crowdproj.comments.biz.general

import com.crowdproj.comments.common.CommentContext
import com.crowdproj.comments.common.helpers.errorAdministration
import com.crowdproj.comments.common.helpers.fail
import com.crowdproj.comments.common.models.CommentCommand
import com.crowdproj.comments.common.models.CommentWorkMode
import com.crowdproj.comments.common.repo.ICommentsRepository
import com.crowdproj.kotlin.cor.handlers.CorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker

fun CorChainDsl<CommentContext>.initRepo(title: String) = worker {
    this.title = title
    description = "Setting repository by requested work mode"
    handle {
        commentRepo = when {
            workMode == CommentWorkMode.STUB -> settings.repoStub
            workMode == CommentWorkMode.TEST -> settings.repoTest
            else -> settings.repoProd
        }
        if (workMode != CommentWorkMode.STUB && commentRepo == ICommentsRepository.NONE && command != CommentCommand.INIT) {
            fail(
                errorAdministration(
                    field = "repo",
                    violationCode = "dbNotConfigured",
                    description = "Repository not configured for chosen work mode ($workMode). " +
                            "Please, contact administrator"
                )
            )
        }
    }
}