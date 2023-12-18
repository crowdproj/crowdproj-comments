package com.crowdproj.comments.biz.validation

import com.crowdproj.comments.biz.CommentProcessor
import com.crowdproj.comments.common.config.CommentsCorSettings
import com.crowdproj.comments.common.models.CommentCommand
import com.crowdproj.comments.repo.stubs.CommentsRepoStub
import kotlin.test.Test

class BusinessValidationUpdateTest {
    val command: CommentCommand = CommentCommand.UPDATE
    private val settings by lazy {
        CommentsCorSettings(
            repoTest = CommentsRepoStub()
        )
    }
    private val processor by lazy { CommentProcessor(settings) }

    @Test fun validationIdsCorrect() = validationIdsCorrect(command, processor)
    @Test fun validationIdsTrim() = validationIdsTrim(command, processor)
    @Test fun validationIdEmpty() = validationIdNotEmpty(command, processor)
    @Test fun validationObjectIdEmpty() = validationObjectIdNotEmpty(command, processor)
    @Test fun validationUserIdEmpty() = validationUserIdNotEmpty(command, processor)
    @Test fun validationBadIdFormat() = validationBadIdFormat(command, processor)
    @Test fun validationBadObjectIdFormat() = validationBadObjectIdFormat(command, processor)
    @Test fun validationBadUserIdFormat() = validationBadUserIdFormat(command, processor)
    @Test fun validationContentCorrect() = validationContentCorrect(command, processor)
    @Test fun validationContentTrim() = validationContentTrim(command, processor)
    @Test fun validationContentEmpty() = validationContentEmpty(command, processor)
    @Test fun validationContentPlainSymbols() = validationContentPlainSymbols(command, processor)
    @Test fun validationBadContentType() = validationBadContentType(command, processor)

}