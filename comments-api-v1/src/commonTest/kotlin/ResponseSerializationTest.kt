package com.crowdproj.comments.api.v1

import com.crowdproj.comments.api.v1.models.*
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class ResponseSerializationTest{
    private val response: ICommentResponse = CommentCreateResponse(
        requestId = "241",
        result = ResponseResult.SUCCESS,
        comment = CommentResponseObject(
            id = "5123",
            objectType = ObjectType.AD,
            objectId = "211124",
            userId = "12111",
            content = "This comment of the ad",
            contentType = ContentType.PLAIN,
            createdAt = "2023-01-11T12:22:53Z",
            updatedAt = "2023-01-11T12:22:53Z"
        )
    )

    @Test
    fun serialize() {
        val json = Json.encodeToString(ICommentResponse.serializer(), response)

        assertContains(json, Regex("\"requestId\":\\s*\"241\""))
        assertContains(json, Regex("\"id\":\\s*\"5123\""))
        assertContains(json, Regex("\"objectType\":\\s*\"ad\""))
        assertContains(json, Regex("\"objectId\":\\s*\"211124\""))
        assertContains(json, Regex("\"userId\":\\s*\"12111\""))
        assertContains(json, Regex("\"content\":\\s*\"This comment of the ad\""))
        assertContains(json, Regex("\"contentType\":\\s*\"plain\""))
        assertContains(json, Regex("\"createdAt\":\\s*\"2023-01-11T12:22:53Z\""))
        assertContains(json, Regex("\"updatedAt\":\\s*\"2023-01-11T12:22:53Z\""))

    }

    @Test
    fun deserialize() {
        val json = Json.encodeToString(ICommentResponse.serializer(), response)
        val obj = Json.decodeFromString(ICommentResponse.serializer(), json) as CommentCreateResponse

        assertEquals(response, obj)
    }
}