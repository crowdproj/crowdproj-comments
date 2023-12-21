package com.crowdproj.comments.app

import com.crowdproj.comments.app.configs.CommentsAppSettings
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class ApplicationTest {

    @Test
    fun root() = testApplication {
        application { module(appSettings = CommentsAppSettings()) }
        val response = client.get("/")

        assertEquals(HttpStatusCode.OK, response.status)
        assertContains(response.bodyAsText(), "CrowdProj Comments API")
    }

}