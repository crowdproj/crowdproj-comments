package com.crowdproj.comments.app.plugins

import com.crowdproj.comments.app.configs.CommentsAppSettings
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.cors.routing.*
import kotlin.reflect.KClass

private val cls: KClass<*> = Application::initCors::class
fun Application.initCors(appSettings: CommentsAppSettings) {
    val logger = appSettings.corSettings.loggerProvider.logger(cls)

    install(CORS) {
        allowNonSimpleContentTypes = true
        allowSameOrigin = true
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Get)
        allowHeader("*")
        appSettings.appUrls.forEach {
            val split = it.split("://")
            println("$split")
            val host = when (split.size) {
                2 -> split[1].split("/")[0].apply {
                    logger.info("COR: $this")
                } to listOf(split[0])

                1 -> split[0].split("/")[0].apply {
                    logger.info("COR: $this")
                } to listOf("http", "https")

                else -> null
            }
            println("ALLOW_HOST: $host")
        }
        allowHost("*")
    }

}
