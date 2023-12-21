package com.crowdproj.comments.repo.tests

import com.crowdproj.comments.common.models.Comment
import com.crowdproj.comments.common.models.CommentObjectId
import com.crowdproj.comments.common.models.CommentObjectType
import com.crowdproj.comments.common.models.CommentUserId
import com.crowdproj.comments.common.repo.DbCommentFilterRequest
import com.crowdproj.comments.common.repo.ICommentsRepository
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

abstract class RepoCommentSearchTest {
    abstract val repo: ICommentsRepository

    @Test
    fun searchObjectType() = runRepoTest {
        val result = repo.searchComments(DbCommentFilterRequest(objectType = CommentObjectType.PRODUCT))
        assertTrue(result.isSuccess)
        val expected = listOf(
            initObjects[1],
            initObjects[2],
        ).sortedBy { it.id.asString() }
        assertEquals(expected, result.data?.sortedBy { it.id.asString() })
        assertTrue(result.errors.isEmpty())
    }

    @Test
    fun searchObjectId() = runRepoTest {
        val result = repo.searchComments(DbCommentFilterRequest(objectId = objectId))
        assertTrue(result.isSuccess)
        val expected = listOf(
            initObjects[3],
            initObjects[4],
        ).sortedBy { it.id.asString() }
        assertEquals(expected, result.data?.sortedBy { it.id.asString() })
        assertTrue(result.errors.isEmpty())
    }

    @Test
    fun searchUserId() = runRepoTest {
        val result = repo.searchComments(DbCommentFilterRequest(userId = userId))
        assertTrue(result.isSuccess)
        val expected = listOf(
            initObjects[5],
            initObjects[6],
        ).sortedBy { it.id.asString() }
        assertEquals(expected, result.data?.sortedBy { it.id.asString() })
        assertTrue(result.errors.isEmpty())
    }

    companion object : BaseInitComments("search") {
        val userId = CommentUserId("user-444")
        val objectId = CommentObjectId("object-333")

        override val initObjects: List<Comment> = listOf(
            createInitTestModel("comment1"),
            createInitTestModel("comment2", objectType = CommentObjectType.PRODUCT),
            createInitTestModel("comment3", objectType = CommentObjectType.PRODUCT),
            createInitTestModel("comment4", objectId = objectId),
            createInitTestModel("comment5", objectId = objectId),
            createInitTestModel("comment6", userId = userId),
            createInitTestModel("comment7", userId = userId),
            createInitTestModel("comment8"),
        )
    }
}