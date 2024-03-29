package com.crowdproj.comments.biz.validation

import com.crowdproj.comments.biz.CommentProcessor
import com.crowdproj.comments.common.CommentContext
import com.crowdproj.comments.common.models.*
import com.crowdproj.comments.common.permissions.CommentsPrincipalModel
import com.crowdproj.comments.common.permissions.CommentsUserGroups
import com.crowdproj.comments.stubs.CommentsStub
import kotlinx.coroutines.test.runTest
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

private val stub = CommentsStub.get()

fun validationContentCorrect(command: CommentCommand, processor: CommentProcessor, commendIdEmpty: Boolean = false) = runTest {
    val ctx = CommentContext(
        command = command,
        state = CommentState.NONE,
        workMode = CommentWorkMode.TEST,
        commentRequest = Comment(
            id = if(commendIdEmpty) CommentId.NONE else stub.id,
            objectId = stub.objectId,
            userId = stub.userId,
            content = "abc",
            contentType = CommentContentType.PLAIN,
        ),
        principal = CommentsPrincipalModel(
            id = stub.userId,
            groups = setOf(
                CommentsUserGroups.USER,
                CommentsUserGroups.MODERATOR
            )
        ),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(CommentState.FAILING, ctx.state)
    assertEquals("abc", ctx.commentValidated.content)
}

fun validationContentTrim(command: CommentCommand, processor: CommentProcessor, commendIdEmpty: Boolean = false) = runTest {
    val ctx = CommentContext(
        command = command,
        state = CommentState.NONE,
        workMode = CommentWorkMode.TEST,
        commentRequest = Comment(
            id = if(commendIdEmpty) CommentId.NONE else stub.id,
            objectId = stub.objectId,
            userId = stub.userId,
            content = " \n\t abc \n\t ",
            contentType = CommentContentType.PLAIN,
        ),
        principal = CommentsPrincipalModel(
            id = stub.userId,
            groups = setOf(
                CommentsUserGroups.USER,
                CommentsUserGroups.MODERATOR
            )
        ),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(CommentState.FAILING, ctx.state)
    assertEquals("abc", ctx.commentValidated.content)
}

fun validationContentEmpty(command: CommentCommand, processor: CommentProcessor, commendIdEmpty: Boolean = false) = runTest {
    val ctx = CommentContext(
        command = command,
        state = CommentState.NONE,
        workMode = CommentWorkMode.TEST,
        commentRequest = Comment(
            id = if(commendIdEmpty) CommentId.NONE else stub.id,
            objectId = stub.objectId,
            userId = stub.userId,
            content = "",
            contentType = CommentContentType.PLAIN,
        ),
        principal = CommentsPrincipalModel(
            id = stub.userId,
            groups = setOf(
                CommentsUserGroups.USER,
                CommentsUserGroups.MODERATOR
            )
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(CommentState.FAILING, ctx.state)
    with(ctx.errors.firstOrNull()) {
        assertEquals("content", this?.field)
        assertEquals(CommentError.Group.VALIDATION , this?.group)
        assertContains(this?.message ?: "", "content")
    }
}

fun validationContentPlainSymbols(command: CommentCommand, processor: CommentProcessor, commendIdEmpty: Boolean = false) = runTest {
    val ctx = CommentContext(
        command = command,
        state = CommentState.NONE,
        workMode = CommentWorkMode.TEST,
        commentRequest = Comment(
            id = if(commendIdEmpty) CommentId.NONE else stub.id,
            objectId = stub.objectId,
            userId = stub.userId,
            content = "@#\$%^&*(),.{}",
            contentType = CommentContentType.PLAIN,
        ),
        principal = CommentsPrincipalModel(
            id = stub.userId,
            groups = setOf(
                CommentsUserGroups.USER,
                CommentsUserGroups.MODERATOR
            )
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(CommentState.FAILING, ctx.state)
    with(ctx.errors.firstOrNull()) {
        assertEquals("content", this?.field)
        assertEquals(CommentError.Group.VALIDATION, this?.group)
        assertContains(this?.message ?: "", "content")
    }
}

fun validationBadContentType(command: CommentCommand, processor: CommentProcessor, commendIdEmpty: Boolean = false) = runTest {
    val ctx = CommentContext(
        command = command,
        state = CommentState.NONE,
        workMode = CommentWorkMode.TEST,
        commentRequest = Comment(
            id = if(commendIdEmpty) CommentId.NONE else stub.id,
            objectId = stub.objectId,
            userId = stub.userId,
            content = "abc",
            contentType = CommentContentType.NONE,
        ),
        principal = CommentsPrincipalModel(
            id = stub.userId,
            groups = setOf(
                CommentsUserGroups.USER,
                CommentsUserGroups.MODERATOR
            )
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(CommentState.FAILING, ctx.state)
    with(ctx.errors.firstOrNull()) {
        assertEquals("contentType", this?.field)
        assertEquals(CommentError.Group.VALIDATION, this?.group)
        assertContains(this?.message ?: "", "contentType")
    }
}

