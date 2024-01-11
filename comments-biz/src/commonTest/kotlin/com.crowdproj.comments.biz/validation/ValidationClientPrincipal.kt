package com.crowdproj.comments.biz.validation

import com.crowdproj.comments.biz.CommentProcessor
import com.crowdproj.comments.common.CommentContext
import com.crowdproj.comments.common.config.CommentsCorSettings
import com.crowdproj.comments.common.models.*
import com.crowdproj.comments.common.permissions.CommentsPrincipalModel
import com.crowdproj.comments.common.permissions.CommentsUserGroups
import com.crowdproj.comments.repo.stubs.CommentsRepoStub
import kotlinx.coroutines.test.runTest
import kotlin.test.*

class ValidationClientPrincipal {
    private val settings by lazy {
        CommentsCorSettings(
            repoTest = CommentsRepoStub()
        )
    }
    private val processor by lazy { CommentProcessor(settings) }

    @Test
    fun validatePrincipalSuccess() = runTest {
        val ctx = CommentContext(
            command = CommentCommand.NONE,
            state = CommentState.NONE,
            workMode = CommentWorkMode.TEST,
            principal = CommentsPrincipalModel(
                id = CommentUserId("123-234-abc-ABC"),
                groups = setOf(CommentsUserGroups.USER),
            )
        )
        processor.exec(ctx)
        assertEquals(0, ctx.errors.size)
        assertEquals(CommentState.RUNNING, ctx.state)
    }

    @Test
    fun validatePrincipalIsEmpty() = runTest {
        val ctx = CommentContext(
            command = CommentCommand.NONE,
            state = CommentState.NONE,
            workMode = CommentWorkMode.TEST
        )
        processor.exec(ctx)
        assertEquals(1, ctx.errors.size)
        assertEquals(CommentState.FAILING, ctx.state)
        with(ctx.errors.firstOrNull()) {
            assertEquals("principal", this?.field)
            assertEquals(CommentError.Group.VALIDATION , this?.group)
            assertContains(this?.message ?: "", "principal")
        }
    }
    @Test
    fun validatePrincipalUserIDIsEmpty() = runTest {
        val ctx = CommentContext(
            command = CommentCommand.NONE,
            state = CommentState.NONE,
            workMode = CommentWorkMode.TEST,
            principal = CommentsPrincipalModel(
                groups = setOf(CommentsUserGroups.USER),
            )
        )
        processor.exec(ctx)
        assertEquals(1, ctx.errors.size)
        assertEquals(CommentState.FAILING, ctx.state)
        with(ctx.errors.firstOrNull()) {
            assertEquals("principal-id", this?.field)
            assertEquals(CommentError.Group.VALIDATION , this?.group)
            assertContains(this?.message ?: "", "principal-id")
        }
    }

    @Test
    fun validatePrincipalGroupsIsEmpty() = runTest {
        val ctx = CommentContext(
            command = CommentCommand.NONE,
            state = CommentState.NONE,
            workMode = CommentWorkMode.TEST,
            principal = CommentsPrincipalModel(
                id = CommentUserId("123"),
            )
        )
        processor.exec(ctx)
        assertEquals(1, ctx.errors.size)
        assertEquals(CommentState.FAILING, ctx.state)
        with(ctx.errors.firstOrNull()) {
            assertEquals("principal-groups", this?.field)
            assertEquals(CommentError.Group.VALIDATION , this?.group)
            assertContains(this?.message ?: "", "principal-groups")
        }
    }
}