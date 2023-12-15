package com.crowdproj.comments.repo.tests

import com.crowdproj.comments.common.models.*
import com.crowdproj.comments.common.repo.DbCommentRequest
import com.crowdproj.comments.common.repo.ICommentsRepository
import kotlin.test.*

abstract class RepoCommentUpdateTest {
    abstract val repo: ICommentsRepository
    protected open val updateSucc = initObjects[0]
    protected open val updateConc = initObjects[1]

    private val reqUpdateSuccess by lazy {
        Comment(
            id = updateSucc.id,
            objectType = CommentObjectType.AD,
            objectId = CommentObjectId("object-321"),
            userId = CommentUserId("user-321"),
            content = "updated comment object",
            contentType = CommentContentType.PLAIN,
            lock = initObjects[0].lock,
        )
    }

    private val reqUpdateConc by lazy {
        Comment(
            id = updateConc.id,
            objectType = CommentObjectType.AD,
            objectId = CommentObjectId("object-321"),
            userId = CommentUserId("user-321"),
            content = "updated comment object",
            contentType = CommentContentType.PLAIN,
            lock = initObjects[1].lock,
        )
    }

    private val reqUpdateNotFound by lazy {
        Comment(
            id = CommentId("comment-repoProd-not-found"),
            objectType = CommentObjectType.AD,
            objectId = CommentObjectId("object-444"),
            userId = CommentUserId("user-444"),
            content = "not updated comment object",
            contentType = CommentContentType.PLAIN,
            lock = initObjects[0].lock,
        )
    }
    @Test
    fun updateSuccess() = runRepoTest {
        val result = repo.updateComment(DbCommentRequest(reqUpdateSuccess))
        assertTrue(result.isSuccess)
        assertEquals(reqUpdateSuccess.id, result.data?.id)
        assertEquals(reqUpdateSuccess.objectType, result.data?.objectType)
        assertEquals(reqUpdateSuccess.objectId, result.data?.objectId)
        assertEquals(reqUpdateSuccess.userId, result.data?.userId)
        assertEquals(reqUpdateSuccess.content, result.data?.content)
        assertEquals(reqUpdateSuccess.contentType, result.data?.contentType)
        assertTrue(result.data?.updatedAt != result.data?.createdAt)
        assertTrue(result.errors.isEmpty())
        assertNotEquals(lockOld, result.data?.lock)
    }

    @Test
    fun updateNotFound() = runRepoTest {
        val result = repo.updateComment(DbCommentRequest(reqUpdateNotFound))
        assertFalse(result.isSuccess)
        assertNull(result.data)
        val error = result.errors.find { it.code == "not-found" }
        assertNotNull(error)
        assertEquals("id", error.field)
    }

    @Test
    fun updateConcurrncyError() = runRepoTest {
        val requestSecond = updateConc.deepCopy()
        val result = repo.updateComment(DbCommentRequest(reqUpdateConc))
        assertTrue(result.isSuccess)
        assertEquals(reqUpdateConc.id, result.data?.id)
        assertEquals(reqUpdateConc.objectType, result.data?.objectType)
        assertEquals(reqUpdateConc.objectId, result.data?.objectId)
        assertEquals(reqUpdateConc.userId, result.data?.userId)
        assertEquals(reqUpdateConc.content, result.data?.content)
        assertEquals(reqUpdateConc.contentType, result.data?.contentType)
        assertTrue(result.errors.isEmpty())
        assertNotEquals(lockOld, result.data?.lock)

        val resultConc = repo.updateComment(DbCommentRequest(requestSecond))
        assertFalse(resultConc.isSuccess)
        val error = resultConc.errors.find { it.code == "concurrency" }
        assertEquals("lock", error?.field)
        assertEquals(result.data, resultConc.data)
    }

    companion object : BaseInitComments("update") {
        override val initObjects: List<Comment> = listOf(
            createInitTestModel("update"),
            createInitTestModel("updateConc")
        )
    }
}