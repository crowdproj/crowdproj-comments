package com.crowdproj.comments.logging.oshai

import com.crowdproj.comments.logging.common.ICommentsLoggerWrapper
import io.github.oshai.kotlinlogging.KotlinLogging

fun loggerKMP(loggerId: String): ICommentsLoggerWrapper {
    val logger = KotlinLogging.logger(loggerId)
    return CommentsLoggerWrapperKermit(

        logger = logger,
        loggerId = loggerId,
    )
}
