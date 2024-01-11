package com.crowdproj.comments.app.common

import com.crowdproj.comments.api.v1.models.*
import com.crowdproj.comments.biz.CommentProcessor
import com.crowdproj.comments.common.config.CommentsCorSettings
import com.crowdproj.comments.common.models.CommentUserId
import com.crowdproj.comments.common.permissions.CommentsPrincipalModel
import com.crowdproj.comments.common.permissions.CommentsUserGroups
import com.crowdproj.comments.mappers.v1.fromTransport
import com.crowdproj.comments.mappers.v1.toTransport
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class ControllerTest {
    private val request = CommentCreateRequest(
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

    private val appSettings: ICommentsAppSettings = object : ICommentsAppSettings{
        override val corSettings: CommentsCorSettings = CommentsCorSettings()
        override val processor: CommentProcessor = CommentProcessor(settings = corSettings)
    }

    class TestApplicationCall(private val request: IRequest) {
        var res: IResponse? = null

        @Suppress("UNCHECKED_CAST")
        fun <T : IRequest> receive(): T = request as T
        fun respond(res: IResponse) {
            this.res = res
        }
    }

    private suspend fun TestApplicationCall.createCommentKtor(appSettings: ICommentsAppSettings) {
        val resp = appSettings.controllerHelper(
            {
                fromTransport(receive<CommentCreateRequest>())
                this.principal = CommentsPrincipalModel(
                    id = CommentUserId("2211"),
                    groups = setOf(CommentsUserGroups.TEST)
                )
            },
            { toTransport() },
            this::class,
            "123"
        )
        respond(resp)
    }

    @Test
    fun ktorHelperTest() = runTest {
        val testApp = TestApplicationCall(request).apply { createCommentKtor(appSettings) }
        val res = testApp.res as CommentCreateResponse
        assertEquals(ResponseResult.SUCCESS, res.result)
    }
}