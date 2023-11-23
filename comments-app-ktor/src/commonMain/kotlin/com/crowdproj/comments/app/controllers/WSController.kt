package com.crowdproj.comments.app.controllers

import com.crowdproj.comments.api.v1.decodeRequest
import com.crowdproj.comments.api.v1.encode
import com.crowdproj.comments.app.common.controllerHelper
import com.crowdproj.comments.app.configs.CommentsAppSettings
import com.crowdproj.comments.common.helpers.isUpdatableCommand
import com.crowdproj.comments.common.models.CommentCommand
import com.crowdproj.comments.mappers.v1.fromTransport
import com.crowdproj.comments.mappers.v1.toTransport
import io.ktor.websocket.*
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.isActive
import kotlin.reflect.KClass

val sessions = mutableSetOf<WebSocketSession>()

private val cls: KClass<*> = WebSocketSession::wsHandler::class
suspend fun WebSocketSession.wsHandler(appSettings: CommentsAppSettings) {
    sessions += this

    appSettings.controllerHelper(
        { command = CommentCommand.INIT },
        { outgoing.send(Frame.Text(toTransport().encode())) },
        cls,
        "wsHandler-init"
    )

    incoming.receiveAsFlow().mapNotNull {
        val frame = it as? Frame.Text ?: return@mapNotNull

        try {
            appSettings.controllerHelper(
                { fromTransport(frame.readText().decodeRequest()) },
                {
                    val result = Frame.Text(toTransport().encode())
                    if (isUpdatableCommand()) {
                        sessions.forEach { session ->
                            if (session.isActive) session.send(result)
                        }
                    } else {
                        outgoing.send(result)
                    }
                },
                cls,
                "wsHandler-message"
            )
        } catch (_: ClosedReceiveChannelException) {
            sessions.clear()
        } catch (e: Throwable) {
            println("Error: ${e.message}")
        }

        appSettings.controllerHelper(
            { command = CommentCommand.FINISH },
            { },
            cls,
            "wsHandler-finish",
        )
    }.collect()
}