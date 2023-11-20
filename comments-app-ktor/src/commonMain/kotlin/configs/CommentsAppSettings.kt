package com.crowdproj.comments.app.configs

import CommentProcessor
import com.crowdproj.comments.common.config.CommentsCorSettings
import commentsApiV1Json
import kotlinx.serialization.json.Json

data class CommentsAppSettings(
    val json: Json = commentsApiV1Json,
    val corSettings: CommentsCorSettings = CommentsCorSettings(),
    val appUrls: List<String> = listOf(),
    val processor: CommentProcessor = CommentProcessor(corSettings)
)
