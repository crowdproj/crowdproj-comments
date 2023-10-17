package stubs

import com.crowdproj.product.comments.api.v1.models.*
import com.crowdproj.product.comments.api.v1.models.ContentType
import commentsApiV1Json
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals

class CommentsStubApiTest {
    @Test
    fun create() = testApplication {
        val client = myClient()

        val response = client.post("/v1/create") {
            val requestObj = CommentCreateRequest(
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
            contentType(io.ktor.http.ContentType.Application.Json)
            setBody(requestObj)
        }
        val responseObj = response.body<CommentCreateResponse>()
        println(responseObj)
        assertEquals(200, response.status.value)
        assertEquals("5312", responseObj.comment?.id)
    }

    @Test
    fun read() = testApplication {
        val client = myClient()

        val response = client.post("/v1/read") {
            val requestObj = CommentReadRequest(
                comment = CommentReadObject(
                    id = "544444"
                ),
                debug = CpBaseDebug(
                    mode = CpRequestDebugMode.STUB,
                    stub = CpRequestDebugStubs.SUCCESS
                )
            )
            contentType(io.ktor.http.ContentType.Application.Json)
            setBody(requestObj)
        }
        val responseObj = response.body<CommentReadResponse>()
        println(responseObj)
        assertEquals(200, response.status.value)
        assertEquals("5312", responseObj.comment?.id)
    }

    @Test
    fun update() = testApplication {
        val client = myClient()

        val response = client.post("/v1/update") {
            val requestObj = CommentUpdateRequest(
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
            contentType(io.ktor.http.ContentType.Application.Json)
            setBody(requestObj)
        }
        val responseObj = response.body<CommentUpdateResponse>()
        println(responseObj)
        assertEquals(200, response.status.value)
        assertEquals("5312", responseObj.comment?.id)
    }

    @Test
    fun delete() = testApplication {
        val client = myClient()

        val response = client.post("/v1/delete") {
            val requestObj = CommentDeleteRequest(
                comment = CommentDeleteObject(
                    id = "544444"
                ),
                debug = CpBaseDebug(
                    mode = CpRequestDebugMode.STUB,
                    stub = CpRequestDebugStubs.SUCCESS
                )
            )
            contentType(io.ktor.http.ContentType.Application.Json)
            setBody(requestObj)
        }
        val responseObj = response.body<CommentDeleteResponse>()
        println(responseObj)
        assertEquals(200, response.status.value)
        assertEquals("5312", responseObj.comment?.id)
    }

    @Test
    fun search() = testApplication {
        val client = myClient()

        val response = client.post("/v1/search") {
            val requestObj = CommentSearchRequest(
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
            contentType(io.ktor.http.ContentType.Application.Json)
            setBody(requestObj)
        }
        val responseObj = response.body<CommentSearchResponse>()
        println(responseObj)
        assertEquals(200, response.status.value)
        assertEquals("cr-111-01", responseObj.comments?.first()?.id)
    }

    private fun ApplicationTestBuilder.myClient() = createClient {
        install(ContentNegotiation) {
            json(commentsApiV1Json)
        }
    }
}