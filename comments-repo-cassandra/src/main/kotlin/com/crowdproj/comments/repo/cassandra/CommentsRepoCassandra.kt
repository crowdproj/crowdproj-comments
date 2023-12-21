package com.crowdproj.comments.repo.cassandra

import com.benasher44.uuid.uuid4
import com.crowdproj.comments.common.helpers.asCommentError
import com.crowdproj.comments.common.helpers.errorRepoConcurrency
import com.crowdproj.comments.common.models.*
import com.crowdproj.comments.common.repo.*
import com.crowdproj.comments.repo.cassandra.helpers.nowMillis
import com.crowdproj.comments.repo.cassandra.models.CasCommentContentType
import com.crowdproj.comments.repo.cassandra.models.CasCommentObjectType
import com.crowdproj.comments.repo.cassandra.models.CommentCassandraDTO
import com.datastax.oss.driver.api.core.CqlSession
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder
import com.datastax.oss.driver.internal.core.type.codec.extras.enums.EnumNameCodec
import com.datastax.oss.driver.internal.core.type.codec.registry.DefaultCodecRegistry
import kotlinx.coroutines.future.await
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import kotlinx.datetime.Clock
import org.slf4j.LoggerFactory
import java.net.InetAddress
import java.net.InetSocketAddress
import java.util.concurrent.CompletionStage
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class CommentsRepoCassandra(
    private val keyspaceName: String,
    private val host: String = "",
    private val port: Int = 9042,
    private val user: String = "cassandra",
    private val pass: String = "cassandra",
    private val testing: Boolean = false,
    private val timeout: Duration = 30.toDuration(DurationUnit.SECONDS),
    initObjects: Collection<Comment> = emptyList(),
) : ICommentsRepository {
    private val randomUuid: () -> String = { uuid4().toString() }

    private val log = LoggerFactory.getLogger(javaClass)

    private val codecRegistry by lazy {
        DefaultCodecRegistry("default").apply {
            register(EnumNameCodec(CasCommentObjectType::class.java))
            register(EnumNameCodec(CasCommentContentType::class.java))
        }
    }

    private val session by lazy {
        CqlSession.builder()
            .addContactPoints(parseAddresses(host, port))
            .withLocalDatacenter("datacenter1")
            .withAuthCredentials(user, pass)
            .withCodecRegistry(codecRegistry)
            .build()
    }

    private val mapper by lazy { CassandraMapper.builder(session).build() }

    private fun createSchema(keyspace: String) {
        session.execute(
            SchemaBuilder
                .createKeyspace(keyspace)
                .ifNotExists()
                .withSimpleStrategy(1)
                .build()
        )
        session.execute(CommentCassandraDTO.table(keyspace, CommentCassandraDTO.TABLE_NAME))
    }

    private val dao by lazy {
        if (testing) {
            createSchema(keyspaceName)
        }
        mapper.commentsDao(keyspaceName, CommentCassandraDTO.TABLE_NAME).apply {
            runBlocking {
                initObjects.map { model ->
                    withTimeout(timeout) {
                        create(CommentCassandraDTO(model)).await()
                    }
                }
            }
        }
    }

    private fun errorToCommentResponse(e: Exception) = DbCommentResponse.error(e.asCommentError())
    private fun errorToCommentsResponse(e: Exception) = DbCommentsResponse.error(e.asCommentError())

    private suspend inline fun <DbRes, Response> doDbAction(
        name: String,
        crossinline daoAction: () -> CompletionStage<DbRes>,
        okToResponse: (DbRes) -> Response,
        errorToResponse: (Exception) -> Response
    ): Response = doDbAction(
        name,
        {
            val dbRes = withTimeout(timeout) { daoAction().await() }
            okToResponse(dbRes)
        },
        errorToResponse
    )

    private inline fun <Response> doDbAction(
        name: String,
        daoAction: () -> Response,
        errorToResponse: (Exception) -> Response
    ): Response =
        try {
            daoAction()
        } catch (e: Exception) {
            log.error("Failed to $name", e)
            errorToResponse(e)
        }

    override suspend fun createComment(request: DbCommentRequest): DbCommentResponse {
        val createdAt = Clock.System.nowMillis()
        val new = request.comment.copy(
            id = CommentId(randomUuid()),
            lock = CommentLock(randomUuid()),
            createdAt = createdAt,
            updatedAt = createdAt
        )
        return doDbAction(
            "create",
            { dao.create(CommentCassandraDTO(new)) },
            { DbCommentResponse.success(new) },
            ::errorToCommentResponse
        )
    }

    override suspend fun readComment(request: DbCommentIdRequest): DbCommentResponse =
        if (request.id == CommentId.NONE)
            RESULT_ID_IS_EMPTY
        else doDbAction(
            "read",
            { dao.read(request.id.asString()) },
            { found ->
                if (found != null) DbCommentResponse.success(found.toCommentModel())
                else RESULT_ID_NOT_FOUND
            },
            ::errorToCommentResponse
        )

    override suspend fun updateComment(request: DbCommentRequest): DbCommentResponse {
        val updatedAt = Clock.System.nowMillis()
        val idStr = request.comment.id.asString()
        val prevLock = request.comment.lock.asString()
        val new = request.comment.copy(
            lock = CommentLock(randomUuid()),
            updatedAt = updatedAt
        )
        val dto = CommentCassandraDTO(new)

        return doDbAction(
            "update",
            {
                val res = dao.update(dto, prevLock).await()
                val isSuccess = res.wasApplied()
                val resultField = res.one()
                    ?.takeIf { it.columnDefinitions.contains(CommentCassandraDTO.COLUMN_LOCK) }
                    ?.getString(CommentCassandraDTO.COLUMN_LOCK)
                    ?.takeIf { it.isNotBlank() }
                when {
                    isSuccess -> DbCommentResponse.success(new)
                    resultField == null -> RESULT_ID_NOT_FOUND
                    else -> DbCommentResponse.error(
                        data = dao.read(idStr).await()?.toCommentModel(),
                        error = errorRepoConcurrency(CommentLock(resultField), request.comment.lock)
                    )
                }
            },
            ::errorToCommentResponse
        )
    }

    override suspend fun deleteComment(request: DbCommentIdRequest): DbCommentResponse {
        return doDbAction(
            "delete",
            {
                val idStr = request.id.asString()
                val prevLock = request.lock.asString()
                val oldComment = dao.read(idStr).await()?.toCommentModel() ?: return@doDbAction RESULT_ID_NOT_FOUND
                val res = dao.delete(idStr, prevLock).await()
                val isSuccess = res.wasApplied()
                val resultField = res.one()
                    ?.takeIf { it.columnDefinitions.contains(CommentCassandraDTO.COLUMN_LOCK) }
                    ?.getString(CommentCassandraDTO.COLUMN_LOCK)
                    ?.takeIf { it.isNotBlank() }
                when {
                    isSuccess -> DbCommentResponse.success(oldComment)
                    resultField == null -> RESULT_ID_NOT_FOUND
                    else -> DbCommentResponse.error(
                        data = dao.read(idStr).await()?.toCommentModel(),
                        error = errorRepoConcurrency(CommentLock(resultField), request.lock)
                    )
                }
            },
            ::errorToCommentResponse
        )
    }

    override suspend fun searchComments(request: DbCommentFilterRequest): DbCommentsResponse =
        doDbAction(
            "search",
            { dao.search(request) },
            { found ->
                DbCommentsResponse.success(found.map { it.toCommentModel() })
            },
            ::errorToCommentsResponse
        )

    companion object {
        private val RESULT_ID_IS_EMPTY = DbCommentResponse.error(
            CommentError(
                code = "id-empty",
                group = CommentError.Group.VALIDATION,
                field = "id",
                message = "Id must not be empty"
            )
        )
        private val RESULT_ID_NOT_FOUND = DbCommentResponse.error(
            CommentError(
                code = "not-found",
                group = CommentError.Group.REQUEST,
                field = "id",
                message = "Not found"
            )
        )
    }
}

private fun parseAddresses(hosts: String, port: Int): Collection<InetSocketAddress> = hosts
    .split(Regex("""\s*,\s*"""))
    .map { InetSocketAddress(InetAddress.getByName(it), port) }