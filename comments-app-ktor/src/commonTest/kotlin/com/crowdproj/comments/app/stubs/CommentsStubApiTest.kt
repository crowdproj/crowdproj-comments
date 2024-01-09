package com.crowdproj.comments.app.stubs

import com.crowdproj.comments.api.v1.commentsApiV1Json
import com.crowdproj.comments.api.v1.models.*
import com.crowdproj.comments.api.v1.models.ContentType
import com.crowdproj.comments.app.configs.CommentsAppSettings
import com.crowdproj.comments.app.module
import com.crowdproj.comments.common.config.CommentsCorSettings
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import io.ktor.util.*
import kotlin.test.Test
import kotlin.test.assertEquals

class CommentsStubApiTest {
    @Test
    fun create() = testApplication {
        val response = getResponse(
            url = "v1/create",
            request = CommentCreateRequest(
                comment = CommentCreateObject(
                    objectType = ObjectType.COMMENT,
                    objectId = "12321",
                    userId = "2211",
                    content = "This is test comment",
                    contentType = ContentType.PLAIN
                ),
                debug = CpBaseDebug(
                    mode = CpRequestDebugMode.STUB,
                    stub = CpRequestDebugStubs.SUCCESS
                )
            )
        )
        val responseObj = response.body<CommentCreateResponse>()
        assertEquals(200, response.status.value)
        assertEquals("5312", responseObj.comment?.id)
    }

    @Test
    fun read() = testApplication {
        val response = getResponse(
            url = "v1/read",
            request = CommentReadRequest(
                comment = CommentReadObject(
                    id = "544444"
                ),
                debug = CpBaseDebug(
                    mode = CpRequestDebugMode.STUB,
                    stub = CpRequestDebugStubs.SUCCESS
                )
            )
        )
        val responseObj = response.body<CommentReadResponse>()
        assertEquals(200, response.status.value)
        assertEquals("544444", responseObj.comment?.id)
    }

    @Test
    fun update() = testApplication {
        val response = getResponse(
            url = "v1/update",
            request = CommentUpdateRequest(
                comment = CommentUpdateObject(
                    objectType = ObjectType.COMMENT,
                    objectId = "12321",
                    userId = "2211",
                    content = "This is test comment",
                    contentType = ContentType.PLAIN
                ),
                debug = CpBaseDebug(
                    mode = CpRequestDebugMode.STUB,
                    stub = CpRequestDebugStubs.SUCCESS
                )
            )
        )
        val responseObj = response.body<CommentUpdateResponse>()
        assertEquals(200, response.status.value)
        assertEquals("5312", responseObj.comment?.id)
    }

    @Test
    fun delete() = testApplication {
        val response = getResponse(
            url = "v1/delete",
            request = CommentDeleteRequest(
                comment = CommentDeleteObject(
                    id = "544444",
                ),
                debug = CpBaseDebug(
                    mode = CpRequestDebugMode.STUB,
                    stub = CpRequestDebugStubs.SUCCESS
                )
            )
        )
        val responseObj = response.body<CommentDeleteResponse>()
        assertEquals(200, response.status.value)
        assertEquals("544444", responseObj.comment?.id)
    }

    @Test
    fun search() = testApplication {
        val response = getResponse(
            url = "v1/search",
            request = CommentSearchRequest(
                commentFilter = CommentSearchFilter(
                    objectType = ObjectType.COMMENT,
                    objectId = "12321",
                    userId = "2211",
                ),
                debug = CpBaseDebug(
                    mode = CpRequestDebugMode.STUB,
                    stub = CpRequestDebugStubs.SUCCESS
                )
            )
        )

        val responseObj = response.body<CommentSearchResponse>()
        assertEquals(200, response.status.value)
        assertEquals("cr-111-01", responseObj.comments?.first()?.id)
    }

    private fun ApplicationTestBuilder.myClient() = createClient {
        install(ContentNegotiation) {
            json(commentsApiV1Json)
        }
    }

    private suspend fun ApplicationTestBuilder.getResponse(url: String, request: ICommentRequest): HttpResponse {
        application {
            module(CommentsAppSettings(corSettings = CommentsCorSettings()))
        }
        val client = myClient()

        val user = "user-123"
        val groups = setOf("Users","Moderators")

        val principalJson = "{\"sub\": \"$user\", \"groups\": [\"${groups.joinToString("\",\"")}\"]}"
        val principalEncoded = principalJson.encodeBase64()

        return client.post(url) {
            contentType(io.ktor.http.ContentType.Application.Json)
            setBody(request)
            headers.append("jwt-parsed", principalEncoded)
        }
    }
}