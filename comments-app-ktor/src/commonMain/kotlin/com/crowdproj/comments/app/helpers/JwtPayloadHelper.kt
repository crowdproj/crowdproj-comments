package com.crowdproj.comments.app.helpers

import com.crowdproj.comments.common.models.CommentUserId
import com.crowdproj.comments.common.permissions.CommentsPrincipalModel
import com.crowdproj.comments.common.permissions.CommentsUserGroups
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonPrimitive
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@OptIn(ExperimentalEncodingApi::class)
fun CommentsPrincipalModel.Companion.fromJwtPayload(jwtPayload: String): CommentsPrincipalModel {
    val payload = Base64.decode(jwtPayload).decodeToString()
    val jsonObject: JsonObject = Json.decodeFromString(payload)
    val id = jsonObject["sub"]?.jsonPrimitive?.content

    val groups = jsonObject["groups"]?.jsonArray?.mapNotNull { jsonElement ->
        jsonElement.jsonPrimitive.content.trim('/').groupFromTransport()
    }?.toSet()

    return CommentsPrincipalModel(
        id = id?.let { CommentUserId(it) } ?: CommentUserId.NONE,
        groups = groups ?: setOf()
    )
}

fun String.groupFromTransport() = when(this) {
    "Users" -> CommentsUserGroups.USER
    "Moderators" -> CommentsUserGroups.MODERATOR
    "Admins" -> CommentsUserGroups.ADMIN_CWP
    "Testers" -> CommentsUserGroups.TEST
    "Banned" -> CommentsUserGroups.BANNED
    else -> null
}