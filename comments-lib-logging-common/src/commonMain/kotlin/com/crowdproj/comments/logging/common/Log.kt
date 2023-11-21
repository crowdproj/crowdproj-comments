package com.crowdproj.comments.logging.common

import kotlinx.datetime.Clock

fun log(
msg: String,
level: LogLevel,
marker: String,
e: Throwable?,
data: Any?,
objs: Map<String, Any>?,
) {
    val markerString = marker
        .takeIf { it.isNotBlank() }
        ?.let { " ($it)" }
    val args = listOfNotNull(
        "${Clock.System.now().toString()} [${level.name}]$markerString: $msg",
        e?.let { "${it.message ?: "Unknown reason"}:\n${it.stackTraceToString()}" },
        data.toString(),
        objs.toString(),
    )
    println(args.joinToString("\n"))
}