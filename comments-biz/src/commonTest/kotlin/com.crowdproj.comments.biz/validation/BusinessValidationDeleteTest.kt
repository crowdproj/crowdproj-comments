package com.crowdproj.comments.biz.validation

import com.crowdproj.comments.biz.CommentProcessor
import com.crowdproj.comments.common.config.CommentsCorSettings
import com.crowdproj.comments.common.models.CommentCommand
import com.crowdproj.comments.repo.stubs.CommentsRepoStub
import kotlin.test.Test

class BusinessValidationDeleteTest {
    val command: CommentCommand = CommentCommand.DELETE
    private val settings by lazy {
        CommentsCorSettings(
            repoTest = CommentsRepoStub()
        )
    }
    private val processor by lazy { CommentProcessor(settings) }

    @Test fun validationIdsCorrect() = validationIdsCorrect(command, processor)
    @Test fun validationIdsTrim() = validationIdsTrim(command, processor)
    @Test fun validationIdEmpty() = validationIdNotEmpty(command, processor)
    @Test fun validationBadIdFormat() = validationBadIdFormat(command, processor)
}