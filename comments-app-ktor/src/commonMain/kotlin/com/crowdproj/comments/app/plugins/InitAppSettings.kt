package com.crowdproj.comments.app.plugins

import com.crowdproj.comments.app.configs.CommentsAppSettings
import com.crowdproj.comments.common.config.CommentsCorSettings
import com.crowdproj.comments.repo.stubs.CommentsRepoStub
import io.ktor.server.application.*

fun Application.initAppSettings(): CommentsAppSettings {
    return CommentsAppSettings(
        appUrls = environment.config
            .propertyOrNull("ktor.urls")
            ?.getList()
            ?.filter { it.isNotBlank() }
            ?: emptyList(),
        corSettings = CommentsCorSettings(
            repoProd = getDatabaseConf(CommentDbType.PROD),
            repoTest = getDatabaseConf(CommentDbType.TEST),
            repoStub = CommentsRepoStub(),
            loggerProvider = getLoggerProviderConf(),
        )
    )
}
