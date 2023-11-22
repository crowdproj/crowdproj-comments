package com.crowdproj.comments.log

import org.slf4j.LoggerFactory
import kotlin.reflect.KClass

actual fun log(
    mes: String,
    clazz: KClass<*>,
    level: LogLevel,
) {
    val log = LoggerFactory.getLogger(clazz.java)
    when(level) {
        LogLevel.TRACE -> log.trace(mes)
        LogLevel.DEBUG -> log.debug(mes)
        LogLevel.INFO -> log.info(mes)
        LogLevel.WARN -> log.warn(mes)
        LogLevel.ERROR -> log.error(mes)
    }

}