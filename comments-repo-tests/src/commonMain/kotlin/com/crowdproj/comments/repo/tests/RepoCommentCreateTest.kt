package com.crowdproj.comments.repo.tests

import com.crowdproj.comments.common.NONE
import com.crowdproj.comments.common.models.*
import com.crowdproj.comments.common.repo.DbCommentRequest
import com.crowdproj.comments.common.repo.ICommentsRepository
import kotlinx.datetime.Instant
import kotlin.test.*

abstract class RepoCommentCreateTest {
    abstract val repo: ICommentsRepository

    private val createObj = Comment(
        objectType = CommentObjectType.AD,
        objectId = CommentObjectId("object-123"),
        userId = CommentUserId("user-123"),
        content = "comment create object",
        contentType = CommentContentType.PLAIN,
    )

    @Test
    fun createSuccess() = runRepoTest {
        val result = repo.createComment(DbCommentRequest(createObj))
        val expected = createObj.copy(id = result.data?.id ?: CommentId.NONE)
        assertEquals(true, result.isSuccess)
        val data = result.data
        assertNotNull(data)
        assertNotEquals(CommentId.NONE, data.id)
        assertEquals(expected.objectType, data.objectType)
        assertEquals(expected.objectId, data.objectId)
        assertEquals(expected.userId, data.userId)
        assertEquals(expected.content, data.content)
        assertEquals(expected.contentType, data.contentType)
        assertNotEquals(data.createdAt, Instant.NONE)
        assertEquals(data.createdAt, data.updatedAt)
        assertTrue(result.errors.isEmpty())
        assertTrue(result.data?.lock?.asString()?.isNotBlank() == true)
    }

    companion object : BaseInitComments("create") {
        override val initObjects: List<Comment> = emptyList()
    }
}