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

fun validationFilterCorrect(command: CommentCommand, processor: CommentProcessor) = runTest {
    val ctx = CommentContext(
        command = command,
        state = CommentState.NONE,
        workMode = CommentWorkMode.TEST,
        commentFilterRequest = CommentFilter(
            objectType = CommentObjectType.PRODUCT,
            objectId = CommentObjectId("123-234-abc-ABC"),
            userId = CommentUserId("123-234-abc-ABC"),
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

fun validationFilterIdsTrim(command: CommentCommand, processor: CommentProcessor) = runTest {
    val ctx = CommentContext(
        command = command,
        state = CommentState.NONE,
        workMode = CommentWorkMode.TEST,
        commentFilterRequest = CommentFilter(
            objectType = CommentObjectType.PRODUCT,
            objectId = CommentObjectId(" \n\t 123-234-abc-ABC \n\t "),
            userId = CommentUserId(" \n\t 123-234-abc-ABC \n\t "),
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

fun validationFilterBadObjectType(command: CommentCommand, processor: CommentProcessor) = runTest {
    val ctx = CommentContext(
        command = command,
        state = CommentState.NONE,
        workMode = CommentWorkMode.TEST,
        commentFilterRequest = CommentFilter(
            objectType = CommentObjectType.NONE,
            objectId = CommentObjectId("123-234-abc-ABC"),
            userId = CommentUserId("123-234-abc-ABC"),
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
    assertEquals(1, ctx.errors.size)
    assertEquals(CommentState.FAILING, ctx.state)
    with(ctx.errors.firstOrNull()) {
        assertEquals("filter-objectType", this?.field)
        assertEquals(CommentError.Group.VALIDATION , this?.group)
        assertContains(this?.message ?: "", "filter-objectType")
    }
}

fun validationFilterNotEmpty(command: CommentCommand, processor: CommentProcessor) = runTest {
    val ctx = CommentContext(
        command = command,
        state = CommentState.NONE,
        workMode = CommentWorkMode.TEST,
        commentFilterRequest = CommentFilter(
            objectType = CommentObjectType.NONE,
            objectId = CommentObjectId(""),
            userId = CommentUserId(""),
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
    assertEquals(2, ctx.errors.size)
    assertEquals(CommentState.FAILING, ctx.state)
    with(ctx.errors.firstOrNull{ it.field == "commentFilter" }) {
        assertEquals("commentFilter", this?.field)
        assertEquals(CommentError.Group.VALIDATION , this?.group)
        assertContains(this?.message ?: "", "commentFilter")
    }
    with(ctx.errors.firstOrNull{ it.field == "filter-objectType" }) {
        assertEquals("filter-objectType", this?.field)
        assertEquals(CommentError.Group.VALIDATION , this?.group)
        assertContains(this?.message ?: "", "filter-objectType")
    }
}

fun validationFilterBadIdsFormat(command: CommentCommand, processor: CommentProcessor) = runTest {
    CommentContext(
        command = command,
        state = CommentState.NONE,
        workMode = CommentWorkMode.TEST,
        commentFilterRequest = CommentFilter(
            objectType = CommentObjectType.PRODUCT,
            objectId = CommentObjectId("!@#\$%^&*(),.{}"),
            userId = CommentUserId("123-234-abc-ABC"),
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
            assertEquals("filter-objectId", this?.field)
            assertEquals(CommentError.Group.VALIDATION , this?.group)
            assertContains(this?.message ?: "", "filter-objectId")
        }
    }

    CommentContext(
        command = command,
        state = CommentState.NONE,
        workMode = CommentWorkMode.TEST,
        commentFilterRequest = CommentFilter(
            objectType = CommentObjectType.PRODUCT,
            objectId = CommentObjectId("123-234-abc-ABC"),
            userId = CommentUserId("!@#\$%^&*(),.{}"),
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
            assertEquals("filter-userId", this?.field)
            assertEquals(CommentError.Group.VALIDATION , this?.group)
            assertContains(this?.message ?: "", "filter-userId")
        }
    }
}