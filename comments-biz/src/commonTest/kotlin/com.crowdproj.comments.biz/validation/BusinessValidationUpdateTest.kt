package com.crowdproj.comments.biz.validation

import com.crowdproj.comments.biz.CommentProcessor
import com.crowdproj.comments.common.models.CommentCommand
import kotlin.test.Test

class BusinessValidationUpdateTest {
    val command: CommentCommand = CommentCommand.UPDATE
    val processor by lazy { CommentProcessor() }

    @Test fun validationIdsCorrect() = validationIdsCorrect(command, processor)
    @Test fun validationIdsTrim() = validationIdsTrim(command, processor)
    @Test fun validationIdEmpty() = validationIdEmpty(command, processor)
    @Test fun validationObjectIdEmpty() = validationObjectIdEmpty(command, processor)
    @Test fun validationUserIdEmpty() = validationUserIdEmpty(command, processor)
    @Test fun validationBadIdFormat() = validationBadIdFormat(command, processor)
    @Test fun validationBadObjectIdFormat() = validationBadObjectIdFormat(command, processor)
    @Test fun validationBadUserIdFormat() = validationBadUserIdFormat(command, processor)
    @Test fun validationContentCorrect() = validationContentCorrect(command, processor)
    @Test fun validationContentTrim() = validationContentTrim(command, processor)
    @Test fun validationContentEmpty() = validationContentEmpty(command, processor)
    @Test fun validationContentPlainSymbols() = validationContentPlainSymbols(command, processor)
    @Test fun validationBadContentType() = validationBadContentType(command, processor)

}