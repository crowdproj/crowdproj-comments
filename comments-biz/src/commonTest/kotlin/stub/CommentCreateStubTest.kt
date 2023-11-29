package ru.otus.otuskotlin.marketplace.biz.stub

import com.crowdproj.comments.biz.CommentProcessor
import com.crowdproj.comments.common.CommentContext
import com.crowdproj.comments.common.models.*
import com.crowdproj.comments.common.stubs.CommentStubs
import com.crowdproj.comments.stubs.CommentStub
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class CommentCreateStubTest {

    private val processor = CommentProcessor()
    val objectType = CommentObjectType.PRODUCT
    val objectId = CommentObjectId("777")
    val userId = CommentUserId("888")
    val content = "Content about product 777"
    val contentType = CommentContentType.PLAIN

    @Test
    fun create() = runTest {

        val ctx = CommentContext(
            command = CommentCommand.CREATE,
            state = CommentState.NONE,
            workMode = CommentWorkMode.STUB,
            stubCase = CommentStubs.SUCCESS,
            commentRequest = Comment(
                objectType = objectType,
                objectId = objectId,
                userId = userId,
                content = content,
                contentType = contentType,
            ),
        )
        processor.exec(ctx)
        assertEquals(CommentStub.get().id, ctx.commentResponse.id)
        assertEquals(objectId, ctx.commentResponse.objectId)
        assertEquals(objectType, ctx.commentResponse.objectType)
        assertEquals(userId, ctx.commentResponse.userId)
        assertEquals(content, ctx.commentResponse.content)
        assertEquals(contentType, ctx.commentResponse.contentType)
    }

    @Test
    fun databaseError() = runTest {
        val ctx = CommentContext(
            command = CommentCommand.CREATE,
            state = CommentState.NONE,
            workMode = CommentWorkMode.STUB,
            stubCase = CommentStubs.DB_ERROR,
            commentRequest = Comment(
                objectType = objectType,
                objectId = objectId,
                userId = userId,
                content = content,
                contentType = contentType,
            ),
        )
        processor.exec(ctx)
        assertEquals(Comment(), ctx.commentResponse)
        assertEquals("internal-db", ctx.errors.firstOrNull()?.code)
        assertEquals(CommentError.Group.SERVER, ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badNoCase() = runTest {
        val ctx = CommentContext(
            command = CommentCommand.CREATE,
            state = CommentState.NONE,
            workMode = CommentWorkMode.STUB,
            stubCase = CommentStubs.BAD_ID,
            commentRequest = Comment(
                objectType = objectType,
                objectId = objectId,
                userId = userId,
                content = content,
                contentType = contentType,
            ),
        )
        processor.exec(ctx)
        assertEquals(Comment(), ctx.commentResponse)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
        assertEquals(CommentError.Group.VALIDATION, ctx.errors.firstOrNull()?.group)
    }
}
