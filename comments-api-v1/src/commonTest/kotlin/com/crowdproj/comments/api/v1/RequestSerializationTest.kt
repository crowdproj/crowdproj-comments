package com.crowdproj.comments.api.v1

import com.crowdproj.comments.api.v1.models.*
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals


class RequestSerializationTest {
    private val request: ICommentRequest = CommentCreateRequest(
        debug = CpBaseDebug(
            mode = CpRequestDebugMode.STUB,
            stub = CpRequestDebugStubs.SUCCESS
        ),
        comment = CommentCreateObject(
            objectType = ObjectType.PRODUCT,
            objectId = "122111",
            content = "This is comment",
            contentType = ContentType.PLAIN,
            userId = "22411"
        )
    )

    @Test
    fun serialize() {
        val json = request.encode()

        assertContains(json, Regex("\"requestType\":\\s*\"create\""))
        assertContains(json, Regex("\"mode\":\\s*\"stub\""))
        assertContains(json, Regex("\"stub\":\\s*\"success\""))
        assertContains(json, Regex("\"objectType\":\\s*\"product\""))
        assertContains(json, Regex("\"objectId\":\\s*\"122111\""))
        assertContains(json, Regex("\"content\":\\s*\"This is comment\""))
        assertContains(json, Regex("\"contentType\":\\s*\"plain\""))
        assertContains(json, Regex("\"userId\":\\s*\"22411\""))
    }

    @Test
    fun deserialize() {
        val json = request.encode()
        val obj = json.decodeRequest<CommentCreateRequest>()

        assertEquals(request, obj)
    }

}