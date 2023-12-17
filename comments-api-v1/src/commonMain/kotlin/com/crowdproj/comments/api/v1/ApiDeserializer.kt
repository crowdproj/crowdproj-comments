package com.crowdproj.comments.api.v1

import com.crowdproj.comments.api.v1.models.IRequest
import com.crowdproj.comments.api.v1.models.IResponse

inline fun <reified T: IResponse> String.decodeResponse (): T = commentsApiV1Json.decodeFromString(this)
inline fun <reified T: IRequest> String.decodeRequest (): T = commentsApiV1Json.decodeFromString(this)