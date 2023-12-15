package com.crowdproj.comments.repo.tests

import com.crowdproj.comments.common.models.Comment
import com.crowdproj.comments.common.models.CommentId
import com.crowdproj.comments.common.repo.DbCommentIdRequest
import com.crowdproj.comments.common.repo.ICommentsRepository
import kotlin.test.*

abstract class RepoCommentReadTest {
    abstract val repo: ICommentsRepository

    protected open val readSucc = initObjects[0]

    @Test
    fun readSuccess() = runRepoTest {
        val result = repo.readComment(DbCommentIdRequest(readSucc.id))

        assertTrue(result.isSuccess)
        assertEquals(readSucc, result.data)
        assertTrue(result.errors.isEmpty())
    }

    @Test
    fun readNotFound() = runRepoTest {
        val result = repo.readComment(DbCommentIdRequest(CommentId("comment-repoProd-not-found")))

        assertFalse(result.isSuccess)
        assertNull(result.data)
        val error = result.errors.find { it.code == "not-found" }
        assertNotNull(error)
        assertEquals("id", error.field)
    }

    companion object : BaseInitComments("read") {
        override val initObjects: List<Comment> = listOf(
            createInitTestModel("read")
        )
    }
}