package com.crowdproj.comments.app.configs

import com.crowdproj.comments.api.v1.commentsApiV1Json
import com.crowdproj.comments.app.common.ICommentsAppSettings
import com.crowdproj.comments.biz.CommentProcessor
import com.crowdproj.comments.common.config.CommentsCorSettings
import kotlinx.serialization.json.Json

data class CommentsAppSettings(
    val json: Json = commentsApiV1Json,
    val corSettings: CommentsCorSettings = CommentsCorSettings(),
    val appUrls: List<String> = listOf(),
    override val processor: CommentProcessor = CommentProcessor()
): ICommentsAppSettings
