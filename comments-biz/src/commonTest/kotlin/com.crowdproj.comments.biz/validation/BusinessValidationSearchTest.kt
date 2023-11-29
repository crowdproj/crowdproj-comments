package com.crowdproj.comments.biz.validation

import com.crowdproj.comments.biz.CommentProcessor
import com.crowdproj.comments.common.models.*
import kotlin.test.Test

class BusinessValidationSearchTest {
    val command: CommentCommand = CommentCommand.SEARCH
    val processor by lazy { CommentProcessor() }

    @Test fun validationFilterCorrect() = validationFilterCorrect(command, processor)
    @Test fun validationFilterIdsTrim() = validationFilterIdsTrim(command, processor)
    @Test fun validationFilterBadObjectType() = validationFilterBadObjectType(command, processor)
    @Test fun validationFilterNotEmpty() = validationFilterNotEmpty(command, processor)
    @Test fun validationFilterBadIdsFormat() = validationFilterBadIdsFormat(command, processor)
}