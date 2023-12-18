package com.crowdproj.comments.biz.stub

import com.crowdproj.comments.biz.CommentProcessor
import com.crowdproj.comments.common.CommentContext
import com.crowdproj.comments.common.models.*
import com.crowdproj.comments.common.stubs.CommentStubs
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class CommentUpdateStubTest {

    private val processor = CommentProcessor()
    val id = CommentId("666")
    val objectType = CommentObjectType.PRODUCT
    val objectId = CommentObjectId("777")
    val userId = CommentUserId("888")
    val content = "Content about product 777"
    val contentType = CommentContentType.PLAIN

    @Test
    fun update() = runTest {

        val ctx = CommentContext(
            command = CommentCommand.UPDATE,
            state = CommentState.NONE,
            workMode = CommentWorkMode.STUB,
            stubCase = CommentStubs.SUCCESS,
            commentRequest = Comment(
                id = id,
                objectType = objectType,
                objectId = objectId,
                userId = userId,
                content = content,
                contentType = contentType,
            ),
        )
        processor.exec(ctx)
        assertEquals(id, ctx.commentResponse.id)
        assertEquals(objectId, ctx.commentResponse.objectId)
        assertEquals(objectType, ctx.commentResponse.objectType)
        assertEquals(userId, ctx.commentResponse.userId)
        assertEquals(content, ctx.commentResponse.content)
        assertEquals(contentType, ctx.commentResponse.contentType)
    }

    @Test
    fun badId() = runTest {
        val ctx = CommentContext(
            command = CommentCommand.UPDATE,
            state = CommentState.NONE,
            workMode = CommentWorkMode.STUB,
            stubCase = CommentStubs.BAD_ID,
            commentRequest = Comment(
                id = CommentId(""),
                objectType = objectType,
                objectId = objectId,
                userId = userId,
                content = content,
                contentType = contentType,
            ),
        )
        processor.exec(ctx)
        assertEquals(Comment(), ctx.commentResponse)
        assertEquals("id", ctx.errors.firstOrNull()?.field)
        assertEquals(CommentError.Group.VALIDATION, ctx.errors.firstOrNull()?.group)
        assertEquals("validation-id-bad", ctx.errors.firstOrNull()?.code)
    }

    @Test
    fun notFound() = runTest {
        val ctx = CommentContext(
            command = CommentCommand.UPDATE,
            state = CommentState.NONE,
            workMode = CommentWorkMode.STUB,
            stubCase = CommentStubs.NOT_FOUND,
            commentRequest = Comment(
                id = id,
                objectType = objectType,
                objectId = objectId,
                userId = userId,
                content = content,
                contentType = contentType,
            ),
        )
        processor.exec(ctx)
        assertEquals(Comment(), ctx.commentResponse)
        assertEquals(CommentError.Group.REQUEST, ctx.errors.firstOrNull()?.group)
        assertEquals("request-not-found", ctx.errors.firstOrNull()?.code)
    }

    @Test
    fun databaseError() = runTest {
        val ctx = CommentContext(
            command = CommentCommand.UPDATE,
            state = CommentState.NONE,
            workMode = CommentWorkMode.STUB,
            stubCase = CommentStubs.DB_ERROR,
            commentRequest = Comment(
                id = id,
                objectType = objectType,
                objectId = objectId,
                userId = userId,
                content = content,
                contentType = contentType,
            ),
        )
        processor.exec(ctx)
        assertEquals(Comment(), ctx.commentResponse)
        assertEquals(CommentError.Group.SERVER, ctx.errors.firstOrNull()?.group)
        assertEquals("internal-db", ctx.errors.firstOrNull()?.code)
    }

    @Test
    fun badNoCase() = runTest {
        val ctx = CommentContext(
            command = CommentCommand.UPDATE,
            state = CommentState.NONE,
            workMode = CommentWorkMode.STUB,
            stubCase = CommentStubs.NONE,
            commentRequest = Comment(
                id = id,
                objectType = objectType,
                objectId = objectId,
                userId = userId,
                content = content,
                contentType = contentType,
            ),
        )
        processor.exec(ctx)
        assertEquals(Comment(), ctx.commentResponse)
        assertEquals(CommentError.Group.VALIDATION, ctx.errors.firstOrNull()?.group)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.code)
    }
}
