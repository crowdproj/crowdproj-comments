package com.crowdproj.comments.repo.tests

import com.crowdproj.comments.common.models.Comment
import com.crowdproj.comments.common.models.CommentId
import com.crowdproj.comments.common.models.CommentLock
import com.crowdproj.comments.common.repo.DbCommentIdRequest
import com.crowdproj.comments.common.repo.ICommentsRepository
import kotlin.test.*

abstract class RepoCommentDeleteTest {
    abstract val repo: ICommentsRepository
    protected open val deleteSucc = initObjects[0]
    protected open val deleteConc = initObjects[1]

    @Test
    fun deleteSuccess() = runRepoTest {
        val lockOld = deleteSucc.lock
        val result = repo.deleteComment(DbCommentIdRequest(deleteSucc.id, deleteSucc.lock))
        assertTrue(result.isSuccess)
        assertTrue(result.errors.isEmpty())
        assertEquals(lockOld, result.data?.lock)
    }

    @Test
    fun deleteNotFound() = runRepoTest {
        val result = repo.deleteComment(DbCommentIdRequest(CommentId("comment-repoProd-not-found"), CommentLock("some-lock")))
        assertFalse(result.isSuccess)
        assertNull(result.data)
        val error = result.errors.find { it.code == "not-found" }
        assertNotNull(error)
        assertEquals("id", error.field)
    }

    @Test
    fun deleteConcurrent() = runRepoTest {
        val lockOld = deleteConc.lock
        val result = repo.deleteComment(DbCommentIdRequest(deleteConc.id, CommentLock("some-lock")), )
        assertFalse(result.isSuccess)
        val error = result.errors.find { it.code == "concurrency" }
        assertNotNull(error)
        assertEquals("lock", error.field)
        assertEquals(lockOld, result.data?.lock)
        assertEquals(deleteConc, result.data)
    }

    companion object : BaseInitComments("delete") {
        override val initObjects: List<Comment> = listOf(
            createInitTestModel("delete"),
            createInitTestModel("deleteConc")
        )
    }
}