package com.crowdproj.comments.app

import kotlinx.serialization.json.*
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlin.test.Test
import kotlin.test.assertEquals

class Jwt {
    val input = "eyJleHAiOjE3MDQxMjQ4ODEsImlhdCI6MTcwNDEyNDU4MSwiYXV0aF90aW1lIjoxNzA0MTI0NDAyLCJqdGkiOiJkYWE0ZmQ1Ni0zMjhjLTQ0OGQtOTA1NS04YzhlZWZkZmE0YzMiLCJpc3MiOiJodHRwOi8vbG9jYWxob3N0OjcwODAvcmVhbG1zL2Nyb3dkcHJvaiIsImF1ZCI6ImFjY291bnQiLCJzdWIiOiIyYzc5YWI2NS05ODU1LTRjMjAtOTE5My0yM2M1MmMxNDkxODAiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJjcm93ZHByb2otY29tbWVudHMiLCJzZXNzaW9uX3N0YXRlIjoiZDlkMzk2NTMtOWUwZS00MjMzLTkwYzUtNTJhMjZmOGE3MzFiIiwiYWNyIjoiMSIsImFsbG93ZWQtb3JpZ2lucyI6WyIvKiJdLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsib2ZmbGluZV9hY2Nlc3MiLCJkZWZhdWx0LXJvbGVzLWNyb3dkcHJvaiIsInVtYV9hdXRob3JpemF0aW9uIiwiY3Jvd2Rwcm9qLXVzZXIiXX0sInJlc291cmNlX2FjY2VzcyI6eyJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50IiwibWFuYWdlLWFjY291bnQtbGlua3MiLCJ2aWV3LXByb2ZpbGUiXX19LCJzY29wZSI6ImVtYWlsIHByb2ZpbGUgZ3JvdXBzIiwic2lkIjoiZDlkMzk2NTMtOWUwZS00MjMzLTkwYzUtNTJhMjZmOGE3MzFiIiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJncm91cHMiOlsiL1VzZXJzIl0sInByZWZlcnJlZF91c2VybmFtZSI6InVzZXIxIn0"
    @OptIn(ExperimentalEncodingApi::class)
    @Test
    fun decode(){


        val result = Base64.decode(input).decodeToString()
        val expected = """{"exp":1704124881,"iat":1704124581,"auth_time":1704124402,"jti":"daa4fd56-328c-448d-9055-8c8eefdfa4c3","iss":"http://localhost:7080/realms/crowdproj","aud":"account","sub":"2c79ab65-9855-4c20-9193-23c52c149180","typ":"Bearer","azp":"crowdproj-comments","session_state":"d9d39653-9e0e-4233-90c5-52a26f8a731b","acr":"1","allowed-origins":["/*"],"realm_access":{"roles":["offline_access","default-roles-crowdproj","uma_authorization","crowdproj-user"]},"resource_access":{"account":{"roles":["manage-account","manage-account-links","view-profile"]}},"scope":"email profile groups","sid":"d9d39653-9e0e-4233-90c5-52a26f8a731b","email_verified":false,"groups":["/Users"],"preferred_username":"user1"}"""
        assertEquals(expected, result)
    }

    @OptIn(ExperimentalEncodingApi::class)
    @Test
    fun parsePayload (){
        val payload = Base64.decode(input).decodeToString()

        val jsonObject: JsonObject = Json.decodeFromString(payload)
        assertEquals("2c79ab65-9855-4c20-9193-23c52c149180", jsonObject["sub"]?.jsonPrimitive?.content)
        val groups = jsonObject["groups"]?.jsonArray?.map { jsonElement ->
            jsonElement.jsonPrimitive.content.trim('/')
        }
        val expectedGroups = listOf("Users")
        assertEquals(expectedGroups, groups)
    }
}