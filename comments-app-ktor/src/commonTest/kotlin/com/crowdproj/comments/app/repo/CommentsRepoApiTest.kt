package com.crowdproj.comments.app.repo

import com.crowdproj.comments.api.v1.commentsApiV1Json
import com.crowdproj.comments.api.v1.models.*
import com.crowdproj.comments.api.v1.models.ContentType as ModelContentType
import com.crowdproj.comments.app.configs.CommentsAppSettings
import com.crowdproj.comments.app.module
import com.crowdproj.comments.common.config.CommentsCorSettings
import com.crowdproj.comments.common.models.*
import com.crowdproj.comments.common.repo.ICommentsRepository
import com.crowdproj.comments.stubs.CommentsStub
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import kotlinx.datetime.Instant
import kotlin.test.*

abstract class CommentsRepoApiTest {

    abstract val repo: ICommentsRepository

    val initComments = listOf(
        CommentsStub.prepareResult {
            id = CommentId("22222222-2222-2222-2222-222222222222")
            objectType = CommentObjectType.PRODUCT
            objectId = CommentObjectId("object-111")
            userId = CommentUserId("user-111")
            content = "comment create first object"
            contentType = CommentContentType.PLAIN
            createdAt = Instant.parse("2022-01-01T12:00:00.00Z")
            updatedAt = createdAt
            lock = CommentLock("55555555-5555-5555-5555-555555555555")
        },
        CommentsStub.prepareResult {
            id = CommentId("11111111-1111-1111-1111-111111111111")
            objectType = CommentObjectType.PRODUCT
            objectId = CommentObjectId("object-222")
            userId = CommentUserId("user-222")
            content = "comment create second object"
            contentType = CommentContentType.PLAIN
            createdAt = Instant.parse("2023-01-01T12:00:00.00Z")
            updatedAt = createdAt
            lock = CommentLock("66666666-6666-6666-6666-666666666666")
        }
    )

    @Test
    fun create() = testApplication {
        application { module(CommentsAppSettings(corSettings = CommentsCorSettings(repoTest = repo, authEnabled = false))) }

        val createComment = CommentCreateObject(
            objectType = ObjectType.AD,
            objectId = "object-123",
            userId = "user-123",
            content = "comment create object",
            contentType = ModelContentType.PLAIN,
        )

        val client = myClient()

        val response = client.post("/v1/create") {
            val requestObj = CommentCreateRequest(
                comment = createComment,
                debug = CpBaseDebug(
                    mode = CpRequestDebugMode.TEST,
                )
            )
            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }
        val responseObj = response.body<CommentCreateResponse>()
        assertEquals(200, response.status.value)
        assertNotNull(responseObj)
        assertNotEquals("", responseObj.comment?.id)
        assertEquals(createComment.objectType, responseObj.comment?.objectType)
        assertEquals(createComment.objectId, responseObj.comment?.objectId)
        assertEquals(createComment.userId, responseObj.comment?.userId)
        assertEquals(createComment.content, responseObj.comment?.content)
        assertEquals(createComment.contentType, responseObj.comment?.contentType)
    }

    @Test
    fun read() = testApplication {
        application {
            module(CommentsAppSettings(corSettings = CommentsCorSettings(repoTest = repo)))
        }
        val client = myClient()

        val response = client.post("/v1/read") {
            val requestObj = CommentReadRequest(
                comment = CommentReadObject(initComments.first().id.asString()),
                debug = CpBaseDebug(
                    mode = CpRequestDebugMode.TEST,
                )
            )
            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }
        val responseObj = response.body<CommentReadResponse>()
        assertEquals(200, response.status.value)
        assertEquals(initComments.first().id.asString(), responseObj.comment?.id)
    }

    @Test
    fun update() = testApplication {
        application {
            module(CommentsAppSettings(corSettings = CommentsCorSettings(repoTest = repo, authEnabled = false)))
        }

        val commentUpdate = CommentUpdateObject(
            id = initComments.first().id.asString(),
            objectType = ObjectType.AD,
            objectId = "object-123",
            userId = "user-123",
            content = "comment create object",
            contentType = ModelContentType.PLAIN,
            lock = initComments.first().lock.asString(),
        )
        val client = myClient()

        val response = client.post("/v1/update") {
            val requestObj = CommentUpdateRequest(
                comment = commentUpdate,
                debug = CpBaseDebug(
                    mode = CpRequestDebugMode.TEST,
                )
            )
            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }
        val responseObj = response.body<CommentUpdateResponse>()
        assertEquals(200, response.status.value)
        assertEquals(commentUpdate.id, responseObj.comment?.id)
        assertEquals(commentUpdate.objectType, responseObj.comment?.objectType)
        assertEquals(commentUpdate.objectId, responseObj.comment?.objectId)
        assertEquals(commentUpdate.userId, responseObj.comment?.userId)
        assertEquals(commentUpdate.content, responseObj.comment?.content)
        assertEquals(commentUpdate.contentType, responseObj.comment?.contentType)
        assertNotEquals(commentUpdate.lock, responseObj.comment?.lock)
    }

    @Test
    fun delete() = testApplication {
        application {
            module(CommentsAppSettings(corSettings = CommentsCorSettings(repoTest = repo, authEnabled = false)))
        }
        val client = myClient()

        val response = client.post("/v1/delete") {
            val requestObj = CommentDeleteRequest(
                comment = CommentDeleteObject(initComments.first().id.asString()),
                debug = CpBaseDebug(
                    mode = CpRequestDebugMode.TEST,
                )
            )
            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }
        val responseObj = response.body<CommentDeleteResponse>()
        assertEquals(200, response.status.value)
        assertEquals(initComments.first().id.asString(), responseObj.comment?.id)
    }

    @Test
    fun search() = testApplication {
        application {
            module(CommentsAppSettings(corSettings = CommentsCorSettings(repoTest = repo, authEnabled = false)))
        }
        val client = myClient()

        val response = client.post("/v1/search") {
            val requestObj = CommentSearchRequest(
                commentFilter = CommentSearchFilter(
                    objectType = ObjectType.PRODUCT,
                ),
                debug = CpBaseDebug(
                    mode = CpRequestDebugMode.TEST,
                )
            )
            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }
        val responseObj = response.body<CommentSearchResponse>()
        assertEquals(200, response.status.value)
        assertEquals(2, responseObj.comments?.size)
    }


    private fun ApplicationTestBuilder.myClient() = createClient {
        install(ContentNegotiation) {
            json(json = commentsApiV1Json)
        }
    }
}