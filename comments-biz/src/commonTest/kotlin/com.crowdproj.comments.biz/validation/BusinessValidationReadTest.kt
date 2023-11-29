package com.crowdproj.comments.biz.validation

import com.crowdproj.comments.biz.CommentProcessor
import com.crowdproj.comments.common.models.CommentCommand
import kotlin.test.Test

class BusinessValidationReadTest {
    private val command: CommentCommand = CommentCommand.READ
    private val processor by lazy { CommentProcessor() }

    @Test fun validationIdsCorrect() = validationIdsCorrect(command, processor)
    @Test fun validationIdsTrim() = validationIdsTrim(command, processor)
    @Test fun validationIdEmpty() = validationIdEmpty(command, processor)
    @Test fun validationBadIdFormat() = validationBadIdFormat(command, processor)
}