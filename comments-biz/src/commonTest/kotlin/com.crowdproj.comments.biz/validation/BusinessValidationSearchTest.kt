package com.crowdproj.comments.biz.validation

import com.crowdproj.comments.biz.CommentProcessor
import com.crowdproj.comments.common.config.CommentsCorSettings
import com.crowdproj.comments.common.models.*
import com.crowdproj.comments.repo.stubs.CommentsRepoStub
import kotlin.test.Test

class BusinessValidationSearchTest {
    val command: CommentCommand = CommentCommand.SEARCH
    private val settings by lazy {
        CommentsCorSettings(
            repoTest = CommentsRepoStub()
        )
    }
    private val processor by lazy { CommentProcessor(settings) }

    @Test fun validationFilterCorrect() = validationFilterCorrect(command, processor)
    @Test fun validationFilterIdsTrim() = validationFilterIdsTrim(command, processor)
    @Test fun validationFilterBadObjectType() = validationFilterBadObjectType(command, processor)
    @Test fun validationFilterNotEmpty() = validationFilterNotEmpty(command, processor)
    @Test fun validationFilterBadIdsFormat() = validationFilterBadIdsFormat(command, processor)
}