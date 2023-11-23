package com.crowdproj.comments.app

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class AppTest {

    @Test
    fun root() = testApplication {
        val response = client.get("/")
        assertEquals(HttpStatusCode.OK, response.status)

        val body = response.body<String>()

        assertContains(body, "SwaggerUI")
    }

}