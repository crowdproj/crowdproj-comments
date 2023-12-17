package com.crowdproj.comments.logging.jvm

import ch.qos.logback.classic.Logger
import com.crowdproj.comments.logging.common.ICommentsLoggerWrapper
import org.slf4j.LoggerFactory

/**
 * Generate internal MpLogContext logger
 *
 * @param logger Logback instance from [LoggerFactory.getLogger()]
 */
fun loggerLogback(logger: Logger): ICommentsLoggerWrapper = CommentsLogWrapperLogback(
    logger = logger,
    loggerId = logger.name,
)

fun loggerLogback(loggerId: String): ICommentsLoggerWrapper = loggerLogback(LoggerFactory.getLogger(loggerId) as Logger)
