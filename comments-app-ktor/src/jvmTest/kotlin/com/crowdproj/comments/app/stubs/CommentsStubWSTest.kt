package com.crowdproj.comments.app.stubs

import com.crowdproj.comments.api.v1.decodeResponse
import com.crowdproj.comments.api.v1.encode
import com.crowdproj.comments.api.v1.models.*
import com.crowdproj.comments.api.v1.models.ContentType
import com.crowdproj.comments.app.configs.CommentsAppSettings
import com.crowdproj.comments.app.module
import com.crowdproj.comments.common.config.CommentsCorSettings
import io.ktor.client.plugins.websocket.*
import io.ktor.server.testing.*
import io.ktor.websocket.*
import kotlinx.coroutines.withTimeout
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class CommentsStubWSTest {
    @Test
    fun create() {
        val request = CommentCreateRequest(
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

        testMethod<CommentCreateResponse>(request) { response ->
            assertEquals("5312", response.comment?.id)
        }
    }

    @Test
    fun read() {
        val request = CommentReadRequest(
            comment = CommentReadObject(
                id = "544444"
            ),
            debug = CpBaseDebug(
                mode = CpRequestDebugMode.STUB,
                stub = CpRequestDebugStubs.SUCCESS
            )
        )

        testMethod<CommentReadResponse>(request) { response ->
            assertEquals("544444", response.comment?.id)
        }
    }

    @Test
    fun update() {
        val request = CommentUpdateRequest(
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

        testMethod<CommentUpdateResponse>(request) { response ->
            assertEquals("5312", response.comment?.id)
        }
    }

    @Test
    fun delete() {
        val request = CommentDeleteRequest(
            comment = CommentDeleteObject(
                id = "544444"
            ),
            debug = CpBaseDebug(
                mode = CpRequestDebugMode.STUB,
                stub = CpRequestDebugStubs.SUCCESS
            )
        )

        testMethod<CommentDeleteResponse>(request) { response ->
            assertEquals("544444", response.comment?.id)
        }
    }

    @Test
    fun search() {

        val request = CommentSearchRequest(
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

        testMethod<CommentSearchResponse>(request) { response ->
            assertEquals("cr-111-01", response.comments?.first()?.id)
        }

    }

    private inline fun <reified T: IResponse> testMethod(
        request: IRequest,
        crossinline assertBlock: (T) -> Unit
    ) = testApplication {
        application { module(CommentsAppSettings(corSettings = CommentsCorSettings(authEnabled = false))) }
        val client = createClient {
            install(WebSockets)
        }

        client.webSocket("/v1/ws") {
            withTimeout(3000) {
                val income = incoming.receive() as Frame.Text
                val response = income.readText().decodeResponse<CommentInitResponse>()
                assertIs<CommentInitResponse>(response)
                assertEquals(ResponseResult.SUCCESS, response.result)
            }
            send(Frame.Text(request.encode()))
            withTimeout(3000) {
                val incame = incoming.receive() as Frame.Text
                val text = incame.readText()
                val response = text.decodeResponse<T>()

                assertBlock(response)
            }
        }
    }
}