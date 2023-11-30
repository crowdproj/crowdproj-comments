package com.crowdproj.comments.api.v1

import com.crowdproj.comments.api.v1.models.IRequest
import com.crowdproj.comments.api.v1.models.IResponse
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

val commentsApiV1Json = Json

fun IResponse.encode() = commentsApiV1Json.encodeToString(this)
fun IRequest.encode() = commentsApiV1Json.encodeToString(this)