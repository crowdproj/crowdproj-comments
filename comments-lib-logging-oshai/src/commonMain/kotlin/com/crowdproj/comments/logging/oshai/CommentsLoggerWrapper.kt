package com.crowdproj.comments.logging.oshai

import com.crowdproj.comments.logging.common.ICommentsLoggerWrapper
import com.crowdproj.comments.logging.common.LogLevel
import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.Marker
import kotlinx.serialization.json.Json

class CommentsLoggerWrapperKermit(
    val logger: KLogger,
    override val loggerId: String
) : ICommentsLoggerWrapper {

    override fun log(
        msg: String,
        level: LogLevel,
        marker: String,
        e: Throwable?,
        data: Any?,
        objs: Map<String, Any>?
    ) {
        log(
            level = level,
            marker = marker,
            throwable = e,
            message = formatMessage(msg, data, objs),
        )
    }

    private fun log(
        level: LogLevel,
        marker: String,
        throwable: Throwable?,
        message: String
    ) {
        when (level) {
            LogLevel.ERROR -> logger.error(
                throwable = throwable,
                marker = SimpleMarker(marker),
            ) { message }

            LogLevel.WARN -> logger.warn(
                throwable = throwable,
                marker = SimpleMarker(marker),
            ) { message }

            LogLevel.INFO -> logger.info(
                throwable = throwable,
                marker = SimpleMarker(marker),
            ) { message }

            LogLevel.DEBUG -> logger.debug(
                throwable = throwable,
                marker = SimpleMarker(marker),
            ) { message }

            LogLevel.TRACE -> logger.trace(
                throwable = throwable,
                marker = SimpleMarker(marker),
            ) { message }
        }
    }

    internal data class SimpleMarker(private val name: String) : Marker {
        override fun getName(): String = this.name
    }

    private fun formatMessage(
        msg: String = "",
        data: Any? = null,
        objs: Map<String, Any>? = null
    ): String {
        var message = msg


        data?.let { d ->
            message += "\n" + try {
                Json.encodeToString(DynamicLookupSerializer, d)
            } catch (e: Throwable) {
                d.toString()
            }
        }
        objs?.forEach {
            message += "\n" + it.toString()
        }
        return message
    }

}