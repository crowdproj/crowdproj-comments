package com.crowdproj.comments.app.plugins

import com.crowdproj.comments.logging.common.CommentsLoggerProvider
import com.crowdproj.comments.logging.oshai.loggerKMP
import io.ktor.server.application.*

actual fun Application.getLoggerProviderConf(): CommentsLoggerProvider = CommentsLoggerProvider {
    loggerKMP(it)
}