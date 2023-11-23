package com.crowdproj.comments.logging.common

import kotlin.reflect.KClass
import kotlin.reflect.KFunction

@Suppress("unused")
class CommentsLoggerProvider(
    private val provider: (String) -> ICommentsLoggerWrapper = { ICommentsLoggerWrapper.DEFAULT}
) {
    fun logger(loggerId: String) = provider(loggerId)
    fun logger(cls: KClass<*>) = provider(cls.qualifiedName ?: cls.simpleName ?: "(unknown)")

    fun logger(function: KFunction<*>) = provider(function.name)
}