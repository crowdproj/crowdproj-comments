package com.crowdproj.comments.app.plugins

import com.crowdproj.comments.app.configs.CommentsAppSettings
import com.crowdproj.comments.common.config.CommentsCorSettings
import com.crowdproj.comments.common.repo.ICommentRepository
import io.ktor.server.application.*

fun Application.initAppSettings(): CommentsAppSettings {
    return CommentsAppSettings(
        appUrls = environment.config
            .propertyOrNull("ktor.urls")
            ?.getList()
            ?.filter { it.isNotBlank() }
            ?: emptyList(),
        corSettings = CommentsCorSettings(
            repo = ICommentRepository.NONE,
            loggerProvider = getLoggerProviderConf()
        )
    )
}
