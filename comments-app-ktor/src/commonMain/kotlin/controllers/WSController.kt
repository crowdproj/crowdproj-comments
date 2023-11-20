package com.crowdproj.comments.app.controllers

import com.crowdproj.comments.api.v1.models.ICommentRequest
import com.crowdproj.comments.app.configs.CommentsAppSettings
import com.crowdproj.comments.common.CommentContext
import com.crowdproj.comments.common.helpers.addError
import com.crowdproj.comments.common.helpers.asCommentError
import com.crowdproj.comments.common.helpers.isUpdatableCommand
import com.crowdproj.comments.common.models.CommentCommand
import decodeRequest
import fromTransport
import io.ktor.websocket.*
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.receiveAsFlow
import encode
import toTransport

val sessions = mutableSetOf<WebSocketSession>()

suspend fun WebSocketSession.wsHandler(appConfig: CommentsAppSettings) {
    sessions += this

    val init = CommentContext().also{
        it.command = CommentCommand.INIT
    }.toTransport().encode()

    outgoing.send(Frame.Text(init))

    incoming.receiveAsFlow().mapNotNull {
        val frame = it as? Frame.Text ?: return@mapNotNull

        val ctx = CommentContext()

        val jsonRequest = frame.readText()

        try{
            val request = jsonRequest.decodeRequest<ICommentRequest>()
            ctx.fromTransport(request)
            appConfig.processor.exec(ctx)
            val result = Frame.Text(ctx.toTransport().encode())

            if(ctx.isUpdatableCommand()){
                sessions.forEach { session ->
                    session.send(result)
                }
            } else {
                outgoing.send(result)
            }
        } catch (_: ClosedReceiveChannelException){
            sessions -= this
        }
        catch (t: Throwable){
            ctx.addError(t.asCommentError())
            val result = Frame.Text(
                (if(ctx.command != CommentCommand.NONE) ctx.toTransport() else ctx.toTransport()).encode()
            )
            outgoing.send(result)
        }
    }.collect()
}