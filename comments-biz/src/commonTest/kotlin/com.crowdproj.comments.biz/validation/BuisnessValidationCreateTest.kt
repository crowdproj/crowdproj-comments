package com.crowdproj.comments.biz.validation

import com.crowdproj.comments.biz.CommentProcessor
import com.crowdproj.comments.common.config.CommentsCorSettings
import com.crowdproj.comments.common.models.CommentCommand
import com.crowdproj.comments.repo.stubs.CommentsRepoStub
import kotlin.test.Test

class BuisnessValidationCreateTest {
    private val command = CommentCommand.CREATE
    private val settings by lazy {
        CommentsCorSettings(
            repoTest = CommentsRepoStub()
        )
    }
    private val processor by lazy { CommentProcessor(settings) }

    @Test fun validationIdNotEmpty() = validationIdEmpty(command, processor)
    @Test fun validationIdsCorrect() = validationIdsCorrect(command, processor, true)
    @Test fun validationIdsTrim() = validationIdsTrim(command, processor, true)
    @Test fun validationObjectIdEmpty() = validationObjectIdNotEmpty(command, processor, true)
    @Test fun validationUserIdEmpty() = validationUserIdNotEmpty(command, processor, true)
    @Test fun validationBadObjectIdFormat() = validationBadObjectIdFormat(command, processor, true)
    @Test fun validationBadUserIdFormat() = validationBadUserIdFormat(command, processor, true)
    @Test fun validationContentCorrect() = validationContentCorrect(command, processor, true)
    @Test fun validationContentTrim() = validationContentTrim(command, processor, true)
    @Test fun validationContentEmpty() = validationContentEmpty(command, processor, true)
    @Test fun validationContentPlainSymbols() = validationContentPlainSymbols(command, processor, true)
    @Test fun validationBadContentType() = validationBadContentType(command, processor, true)
}