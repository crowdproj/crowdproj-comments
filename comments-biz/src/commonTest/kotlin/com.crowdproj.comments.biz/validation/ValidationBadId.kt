package com.crowdproj.comments.biz.validation

import com.crowdproj.comments.biz.CommentProcessor
import com.crowdproj.comments.common.CommentContext
import com.crowdproj.comments.common.models.*
import com.crowdproj.comments.common.permissions.CommentsPrincipalModel
import com.crowdproj.comments.common.permissions.CommentsUserGroups
import kotlinx.coroutines.test.runTest
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

fun validationIdsCorrect(command: CommentCommand, processor: CommentProcessor, commendIdEmpty: Boolean = false) = runTest {
    val ctx = CommentContext(
        command = command,
        state = CommentState.NONE,
        workMode = CommentWorkMode.TEST,
        commentRequest = Comment(
            id = CommentId(
                if(commendIdEmpty) "" else "123-234-abc-ABC"
            ),
            objectId = CommentObjectId("123-234-abc-ABC"),
            userId = CommentUserId("123-234-abc-ABC"),
            content = "abc",
            contentType = CommentContentType.PLAIN,
        ),
        principal = CommentsPrincipalModel(
            id = CommentUserId("123-234-abc-ABC"),
            groups = setOf(
                CommentsUserGroups.USER,
                CommentsUserGroups.MODERATOR
            )
        ),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(CommentState.FAILING, ctx.state)
}

fun validationIdsTrim(command: CommentCommand, processor: CommentProcessor, commendIdEmpty: Boolean = false) = runTest {
    val ctx = CommentContext(
        command = command,
        state = CommentState.NONE,
        workMode = CommentWorkMode.TEST,
        commentRequest = Comment(
            id = CommentId(" \n\t ${if(commendIdEmpty) "" else "123-234-abc-ABC"} \n\t "),
            objectId = CommentObjectId(" \n\t 123-234-abc-ABC \n\t "),
            userId = CommentUserId(" \n\t 123-234-abc-ABC \n\t "),
            content = "abc",
            contentType = CommentContentType.PLAIN,
        ),
        principal = CommentsPrincipalModel(
            id = CommentUserId("123-234-abc-ABC"),
            groups = setOf(
                CommentsUserGroups.USER,
                CommentsUserGroups.MODERATOR
            )
        ),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(CommentState.FAILING, ctx.state)
}

fun validationIdEmpty(command: CommentCommand, processor: CommentProcessor) = runTest {
    CommentContext(
        command = command,
        state = CommentState.NONE,
        workMode = CommentWorkMode.TEST,
        commentRequest = Comment(
            id = CommentId("123-234-abc-ABC"),
            objectId = CommentObjectId("123-234-abc-ABC"),
            userId = CommentUserId("123-234-abc-ABC"),
            content = "abc",
            contentType = CommentContentType.PLAIN,
        ),
        principal = CommentsPrincipalModel(
            id = CommentUserId("123-234-abc-ABC"),
            groups = setOf(
                CommentsUserGroups.USER,
                CommentsUserGroups.MODERATOR
            )
        ),
    ).also { ctx ->
        processor.exec(ctx)
        assertEquals(1, ctx.errors.size)
        assertEquals(CommentState.FAILING, ctx.state)
        with(ctx.errors.firstOrNull()) {
            assertEquals("id", this?.field)
            assertEquals(CommentError.Group.VALIDATION , this?.group)
            assertContains(this?.message ?: "", "id")
        }
    }
}

fun validationIdNotEmpty(command: CommentCommand, processor: CommentProcessor) = runTest {
    CommentContext(
        command = command,
        state = CommentState.NONE,
        workMode = CommentWorkMode.TEST,
        commentRequest = Comment(
            id = CommentId(""),
            objectId = CommentObjectId("123-234-abc-ABC"),
            userId = CommentUserId("123-234-abc-ABC"),
            content = "abc",
            contentType = CommentContentType.PLAIN,
        ),
        principal = CommentsPrincipalModel(
            id = CommentUserId("123-234-abc-ABC"),
            groups = setOf(
                CommentsUserGroups.USER,
                CommentsUserGroups.MODERATOR
            )
        ),
    ).also { ctx ->
        processor.exec(ctx)
        assertEquals(1, ctx.errors.size)
        assertEquals(CommentState.FAILING, ctx.state)
        with(ctx.errors.firstOrNull()) {
            assertEquals("id", this?.field)
            assertEquals(CommentError.Group.VALIDATION , this?.group)
            assertContains(this?.message ?: "", "id")
        }
    }
}

fun validationObjectIdNotEmpty(command: CommentCommand, processor: CommentProcessor, commendIdEmpty: Boolean = false) = runTest {
    CommentContext(
        command = command,
        state = CommentState.NONE,
        workMode = CommentWorkMode.TEST,
        commentRequest = Comment(
            id = CommentId(if(commendIdEmpty) "" else "123-234-abc-ABC"),
            objectId = CommentObjectId(""),
            userId = CommentUserId("123-234-abc-ABC"),
            content = "abc",
            contentType = CommentContentType.PLAIN,
        ),
        principal = CommentsPrincipalModel(
            id = CommentUserId("123-234-abc-ABC"),
            groups = setOf(
                CommentsUserGroups.USER,
                CommentsUserGroups.MODERATOR
            )
        ),
    ).also { ctx ->
        processor.exec(ctx)
        assertEquals(1, ctx.errors.size)
        assertEquals(CommentState.FAILING, ctx.state)
        with(ctx.errors.firstOrNull()) {
            assertEquals("objectId", this?.field)
            assertEquals(CommentError.Group.VALIDATION , this?.group)
            assertContains(this?.message ?: "", "objectId")
        }
    }
}

fun validationUserIdNotEmpty(command: CommentCommand, processor: CommentProcessor, commendIdEmpty: Boolean = false) = runTest {
    CommentContext(
        command = command,
        state = CommentState.NONE,
        workMode = CommentWorkMode.TEST,
        commentRequest = Comment(
            id = CommentId(if(commendIdEmpty) "" else "123-234-abc-ABC"),
            objectId = CommentObjectId("123-234-abc-ABC"),
            userId = CommentUserId(""),
            content = "abc",
            contentType = CommentContentType.PLAIN,
        ),
        principal = CommentsPrincipalModel(
            id = CommentUserId("123-234-abc-ABC"),
            groups = setOf(
                CommentsUserGroups.USER,
                CommentsUserGroups.MODERATOR
            )
        ),
    ).also { ctx ->
        processor.exec(ctx)
        assertEquals(1, ctx.errors.size)
        assertEquals(CommentState.FAILING, ctx.state)
        with(ctx.errors.firstOrNull()) {
            assertEquals("userId", this?.field)
            assertEquals(CommentError.Group.VALIDATION , this?.group)
            assertContains(this?.message ?: "", "userId")
        }
    }
}

fun validationBadIdFormat(command: CommentCommand, processor: CommentProcessor) = runTest {
    CommentContext(
        command = command,
        state = CommentState.NONE,
        workMode = CommentWorkMode.TEST,
        commentRequest = Comment(
            id = CommentId("!@#\$%^&*(),.{}"),
            objectId = CommentObjectId("123-234-abc-ABC"),
            userId = CommentUserId("123-234-abc-ABC"),
            content = "abc",
            contentType = CommentContentType.PLAIN,
        ),
        principal = CommentsPrincipalModel(
            id = CommentUserId("123-234-abc-ABC"),
            groups = setOf(
                CommentsUserGroups.USER,
                CommentsUserGroups.MODERATOR
            )
        ),
    ).also { ctx ->
        processor.exec(ctx)
        assertEquals(1, ctx.errors.size)
        assertEquals(CommentState.FAILING, ctx.state)
        with(ctx.errors.firstOrNull()) {
            assertEquals("id", this?.field)
            assertEquals(CommentError.Group.VALIDATION , this?.group)
            assertContains(this?.message ?: "", "id")
        }
    }
}

fun validationBadObjectIdFormat(command: CommentCommand, processor: CommentProcessor, commendIdEmpty: Boolean = false) = runTest {
    CommentContext(
        command = command,
        state = CommentState.NONE,
        workMode = CommentWorkMode.TEST,
        commentRequest = Comment(
            id = CommentId(if(commendIdEmpty) "" else "123-234-abc-ABC"),
            objectId = CommentObjectId("!@#\$%^&*(),.{}"),
            userId = CommentUserId("123-234-abc-ABC"),
            content = "abc",
            contentType = CommentContentType.PLAIN,
        ),
        principal = CommentsPrincipalModel(
            id = CommentUserId("123-234-abc-ABC"),
            groups = setOf(
                CommentsUserGroups.USER,
                CommentsUserGroups.MODERATOR
            )
        ),
    ).also { ctx ->
        processor.exec(ctx)
        assertEquals(1, ctx.errors.size)
        assertEquals(CommentState.FAILING, ctx.state)
        with(ctx.errors.firstOrNull()) {
            assertEquals("objectId", this?.field)
            assertEquals(CommentError.Group.VALIDATION , this?.group)
            assertContains(this?.message ?: "", "objectId")
        }
    }
}

fun validationBadUserIdFormat(command: CommentCommand, processor: CommentProcessor, commendIdEmpty: Boolean = false) = runTest {
    CommentContext(
        command = command,
        state = CommentState.NONE,
        workMode = CommentWorkMode.TEST,
        commentRequest = Comment(
            id = CommentId(if(commendIdEmpty) "" else "123-234-abc-ABC"),
            objectId = CommentObjectId("123-234-abc-ABC"),
            userId = CommentUserId("!@#\$%^&*(),.{}"),
            content = "abc",
            contentType = CommentContentType.PLAIN,
        ),
        principal = CommentsPrincipalModel(
            id = CommentUserId("123-234-abc-ABC"),
            groups = setOf(
                CommentsUserGroups.USER,
                CommentsUserGroups.MODERATOR
            )
        ),
    ).also { ctx ->
        processor.exec(ctx)
        assertEquals(1, ctx.errors.size)
        assertEquals(CommentState.FAILING, ctx.state)
        with(ctx.errors.firstOrNull()) {
            assertEquals("userId", this?.field)
            assertEquals(CommentError.Group.VALIDATION , this?.group)
            assertContains(this?.message ?: "", "userId")
        }
    }
}