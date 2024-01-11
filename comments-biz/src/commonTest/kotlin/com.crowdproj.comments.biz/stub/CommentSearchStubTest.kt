package com.crowdproj.comments.biz.stub

import com.crowdproj.comments.biz.CommentProcessor
import com.crowdproj.comments.common.CommentContext
import com.crowdproj.comments.common.models.*
import com.crowdproj.comments.common.permissions.CommentsPrincipalModel
import com.crowdproj.comments.common.permissions.CommentsUserGroups
import com.crowdproj.comments.common.stubs.CommentStubs
import com.crowdproj.comments.stubs.CommentsStub
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail

class CommentSearchStubTest {

    private val processor = CommentProcessor()
    val filter = CommentFilter(
        objectType = CommentObjectType.PRODUCT,
        objectId = CommentObjectId("777"),
        userId = CommentUserId("888")
    )

    @Test
    fun read() = runTest {

        val ctx = CommentContext(
            command = CommentCommand.SEARCH,
            state = CommentState.NONE,
            workMode = CommentWorkMode.STUB,
            stubCase = CommentStubs.SUCCESS,
            commentFilterRequest = filter,
            principal = CommentsPrincipalModel(
                id = CommentUserId("123-234-abc-ABC"),
                groups = setOf(
                    CommentsUserGroups.USER,
                    CommentsUserGroups.MODERATOR
                )
            ),
        )
        processor.exec(ctx)
        assertTrue(ctx.commentsResponse.size > 1)
        CommentsStub.prepareSearchList(
            filterObjectType = filter.objectType,
            filterObjectId = filter.objectId.asString(),
            filterUserId = filter.userId.asString()
        ).also { stubList ->
            assertEquals(stubList.size, ctx.commentsResponse.size)
            stubList.forEach { comment ->
                with(ctx.commentsResponse.firstOrNull { it.id == comment.id } ?: fail("Not found in response")) {
                    assertEquals(comment.id, id)
                    assertEquals(comment.content, content)
                    assertEquals(comment.contentType, contentType)
                    assertEquals(filter.objectType, objectType)
                    assertEquals(filter.objectId, objectId)
                    assertEquals(filter.userId, userId)
                }
            }
        }

    }

    @Test
    fun badId() = runTest {
        val ctx = CommentContext(
            command = CommentCommand.SEARCH,
            state = CommentState.NONE,
            workMode = CommentWorkMode.STUB,
            stubCase = CommentStubs.BAD_ID,
            commentFilterRequest = filter,
            principal = CommentsPrincipalModel(
                id = CommentUserId("123-234-abc-ABC"),
                groups = setOf(
                    CommentsUserGroups.USER,
                    CommentsUserGroups.MODERATOR
                )
            ),
        )
        processor.exec(ctx)
        assertEquals(Comment(), ctx.commentResponse)
        assertTrue(ctx.commentsResponse.isEmpty())
        assertEquals("id", ctx.errors.firstOrNull()?.field)
        assertEquals(CommentError.Group.VALIDATION, ctx.errors.firstOrNull()?.group)
        assertEquals("validation-id-bad", ctx.errors.firstOrNull()?.code)
    }

    @Test
    fun notFound() = runTest {
        val ctx = CommentContext(
            command = CommentCommand.SEARCH,
            state = CommentState.NONE,
            workMode = CommentWorkMode.STUB,
            stubCase = CommentStubs.NOT_FOUND,
            commentFilterRequest = filter,
            principal = CommentsPrincipalModel(
                id = CommentUserId("123-234-abc-ABC"),
                groups = setOf(
                    CommentsUserGroups.USER,
                    CommentsUserGroups.MODERATOR
                )
            ),
        )
        processor.exec(ctx)
        assertEquals(Comment(), ctx.commentResponse)
        assertTrue(ctx.commentsResponse.isEmpty())
        assertEquals(CommentError.Group.REQUEST, ctx.errors.firstOrNull()?.group)
        assertEquals("request-not-found", ctx.errors.firstOrNull()?.code)
    }

    @Test
    fun databaseError() = runTest {
        val ctx = CommentContext(
            command = CommentCommand.SEARCH,
            state = CommentState.NONE,
            workMode = CommentWorkMode.STUB,
            stubCase = CommentStubs.DB_ERROR,
            commentFilterRequest = filter,
            principal = CommentsPrincipalModel(
                id = CommentUserId("123-234-abc-ABC"),
                groups = setOf(
                    CommentsUserGroups.USER,
                    CommentsUserGroups.MODERATOR
                )
            ),
        )
        processor.exec(ctx)
        assertEquals(Comment(), ctx.commentResponse)
        assertTrue(ctx.commentsResponse.isEmpty())
        assertEquals(CommentError.Group.SERVER, ctx.errors.firstOrNull()?.group)
        assertEquals("internal-db", ctx.errors.firstOrNull()?.code)
    }

    @Test
    fun badNoCase() = runTest {
        val ctx = CommentContext(
            command = CommentCommand.SEARCH,
            state = CommentState.NONE,
            workMode = CommentWorkMode.STUB,
            stubCase = CommentStubs.NONE,
            commentFilterRequest = filter,
            principal = CommentsPrincipalModel(
                id = CommentUserId("123-234-abc-ABC"),
                groups = setOf(
                    CommentsUserGroups.USER,
                    CommentsUserGroups.MODERATOR
                )
            ),
        )
        processor.exec(ctx)
        assertEquals(Comment(), ctx.commentResponse)
        assertTrue(ctx.commentsResponse.isEmpty())
        assertEquals(CommentError.Group.VALIDATION, ctx.errors.firstOrNull()?.group)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.code)
    }
}
