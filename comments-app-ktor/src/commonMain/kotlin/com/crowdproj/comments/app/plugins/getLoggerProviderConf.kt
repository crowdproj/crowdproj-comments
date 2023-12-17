package com.crowdproj.comments.app.plugins

import com.crowdproj.comments.logging.common.CommentsLoggerProvider
import io.ktor.server.application.*

expect fun Application.getLoggerProviderConf(): CommentsLoggerProvider