package com.crowdproj.comments.repo.inmemory

import com.benasher44.uuid.uuid4
import com.crowdproj.comments.common.helpers.errorRepoConcurrency
import com.crowdproj.comments.common.models.*
import com.crowdproj.comments.common.repo.*
import com.crowdproj.comments.repo.inmemory.model.CommentEntity
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import io.github.reactivecircus.cache4k.Cache
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.datetime.Clock

class CommentsRepoInMemory(
    initObjects: List<Comment> = emptyList(),
    ttl: Duration = 2.minutes,
) : ICommentsRepository {
    val randomUuid: () -> String = { uuid4().toString() }
    private val cache = Cache.Builder<String, CommentEntity>()
        .expireAfterWrite(ttl)
        .build()

    private val mutex: Mutex = Mutex()

    init {
        initObjects.forEach {
            save(it)
        }
    }

    private fun save(comment: Comment) {
        val entity = CommentEntity(comment)
        if (entity.id == null) {
            return
        }
        cache.put(entity.id, entity)
    }

    override suspend fun createComment(request: DbCommentRequest): DbCommentResponse {
        val createdAt = Clock.System.now()
        val id = randomUuid()
        val comment = request.comment.copy(
            id = CommentId(id),
            lock = CommentLock(randomUuid()),
            createdAt = createdAt,
            updatedAt = createdAt
        )
        val entity = CommentEntity(comment)
        cache.put(id, entity)
        return DbCommentResponse(
            data = comment,
            isSuccess = true,
        )
    }

    override suspend fun readComment(request: DbCommentIdRequest): DbCommentResponse {
        val id = request.id.takeIf { it != CommentId.NONE }?.asString() ?: return RESULT_ID_IS_EMPTY
        return cache.get(id)?.let {
            DbCommentResponse(
                data = it.toInternal(),
                isSuccess = true,
            )
        } ?: RESULT_ID_NOT_FOUND
    }

    override suspend fun updateComment(request: DbCommentRequest): DbCommentResponse {
        val updatedAt = Clock.System.now()
        val id = request.comment.id.takeIf { it != CommentId.NONE }?.asString() ?: return RESULT_ID_IS_EMPTY
        val oldLock = request.comment.lock.takeIf { it != CommentLock.NONE }?.asString() ?: return RESULT_LOCK_IS_EMPTY
        val newComment = request.comment.copy(
            lock = CommentLock(randomUuid()),
            updatedAt = updatedAt
        )
        val entity = CommentEntity(newComment)

        return mutex.withLock {
            val oldComment = cache.get(id)
            when {
                oldComment == null -> RESULT_ID_NOT_FOUND
                oldComment.lock != oldLock -> DbCommentResponse(
                    data = oldComment.toInternal(),
                    isSuccess = false,
                    errors = listOf(
                        errorRepoConcurrency(CommentLock(oldLock), oldComment.lock?.let { CommentLock(it) })
                    )
                )
                else -> {
                    cache.put(id, entity)
                    DbCommentResponse(
                        data = newComment,
                        isSuccess = true
                    )
                }
            }
        }
    }

    override suspend fun deleteComment(request: DbCommentIdRequest): DbCommentResponse {
        val id = request.id.takeIf { it != CommentId.NONE }?.asString() ?: return RESULT_ID_IS_EMPTY
        val oldLock = request.lock.takeIf { it != CommentLock.NONE }?.asString() ?: return RESULT_LOCK_IS_EMPTY
        return mutex.withLock {
            val oldComment = cache.get(id)
            when {
                oldComment == null -> RESULT_ID_NOT_FOUND
                oldComment.lock != oldLock -> DbCommentResponse(
                    data = oldComment.toInternal(),
                    isSuccess = false,
                    errors = listOf(
                        errorRepoConcurrency(CommentLock(oldLock), oldComment.lock?.let { CommentLock(it) })
                    )
                )
                else -> {
                    cache.invalidate(id)
                    DbCommentResponse(
                        data = oldComment.toInternal(),
                        isSuccess = true
                    )
                }
            }
        }
    }

    override suspend fun searchComments(request: DbCommentFilterRequest): DbCommentsResponse {
        val result = cache.asMap().asSequence()
            .filter { entry ->
                request.objectType.takeIf { it != CommentObjectType.NONE }?.let { objectType ->
                    objectType.name == entry.value.objectType
                } ?: true
            }
            .filter { entry ->
                request.objectId.takeIf { it != CommentObjectId.NONE }?.let { objectId ->
                    objectId.asString() == entry.value.objectId
                } ?: true
            }
            .filter { entry ->
                request.userId.takeIf { it != CommentUserId.NONE }?.let { userId ->
                    userId.asString() == entry.value.userId
                } ?: true
            }
            .map { it.value.toInternal() }
            .toList()
        return DbCommentsResponse(
            data = result,
            isSuccess = true,
        )
    }

    companion object {
        val RESULT_ID_IS_EMPTY = DbCommentResponse(
            data = null,
            isSuccess = false,
            errors = listOf(
                CommentError(
                    code = "id-empty",
                    group = CommentError.Group.VALIDATION,
                    field = "id",
                    message = "Id must not be empty"
                )
            )
        )
        val RESULT_ID_NOT_FOUND = DbCommentResponse(
            data = null,
            isSuccess = false,
            errors = listOf(
                CommentError(
                    code = "not-found",
                    group = CommentError.Group.REQUEST,
                    field = "id",
                    message = "Not found"
                )
            )
        )
        val RESULT_LOCK_IS_EMPTY = DbCommentResponse(
            data = null,
            isSuccess = false,
            errors = listOf(
                CommentError(
                    code = "lock-empty",
                    group = CommentError.Group.VALIDATION,
                    field = "lock",
                    message = "Lock must not be empty"
                )
            )
        )
    }
}