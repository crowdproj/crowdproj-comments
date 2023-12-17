package com.crowdproj.comments.app.plugins

import com.crowdproj.comments.logging.common.CommentsLoggerProvider
import com.crowdproj.comments.logging.jvm.loggerLogback
import com.crowdproj.comments.logging.oshai.loggerKMP
import io.ktor.server.application.*

actual fun Application.getLoggerProviderConf(): CommentsLoggerProvider =
    when (val mode = environment.config.propertyOrNull("ktor.logger")?.getString()) {
        "kmp" -> CommentsLoggerProvider { loggerKMP(it) }
        "logback", null -> CommentsLoggerProvider { loggerLogback(it) }
        else -> throw Exception("Logger $mode is not allowed. Additted values are kmp and logback")
    }
