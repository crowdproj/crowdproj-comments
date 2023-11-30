package com.crowdproj.comments.app.controllers

import com.crowdproj.comments.api.v1.models.*
import com.crowdproj.comments.app.configs.CommentsAppSettings
import com.crowdproj.comments.app.helpers.processV1
import io.ktor.server.application.*
import kotlin.reflect.KFunction

private val clCreate: KFunction<*> = ApplicationCall::createComment
suspend fun ApplicationCall.createComment(appSettings: CommentsAppSettings) =
    processV1<CommentCreateRequest, CommentCreateResponse>(appSettings, clCreate, "createComment")

private val clRead: KFunction<*> = ApplicationCall::readComment
suspend fun ApplicationCall.readComment(appSettings: CommentsAppSettings) =
    processV1<CommentReadRequest, CommentReadResponse>(appSettings, clRead, "readComment")

private val clUpdate: KFunction<*> = ApplicationCall::updateComment
suspend fun ApplicationCall.updateComment(appSettings: CommentsAppSettings) =
    processV1<CommentUpdateRequest, CommentUpdateResponse>(appSettings, clUpdate, "updateComment")

private val clDelete: KFunction<*> = ApplicationCall::deleteComment
suspend fun ApplicationCall.deleteComment(appSettings: CommentsAppSettings) =
    processV1<CommentDeleteRequest, CommentDeleteResponse>(appSettings, clDelete, "deleteComment")

private val clSearch: KFunction<*> = ApplicationCall::searchComment
suspend fun ApplicationCall.searchComment(appSettings: CommentsAppSettings) =
    processV1<CommentSearchRequest, CommentSearchResponse>(appSettings, clSearch, "searchComment")