package com.crowdproj.comments.app.controllers

import com.crowdproj.comments.api.v1.models.*
import com.crowdproj.comments.app.configs.CommentsAppSettings
import com.crowdproj.comments.app.helpers.processV1
import io.ktor.server.application.*
import kotlin.reflect.KClass

private val clCreate: KClass<*> = ApplicationCall::createComment::class
suspend fun ApplicationCall.createComment(appSettings: CommentsAppSettings) =
    processV1<CommentCreateRequest, CommentCreateResponse>(appSettings, clCreate, "createComment")

private val clRead: KClass<*> = ApplicationCall::readComment::class
suspend fun ApplicationCall.readComment(appSettings: CommentsAppSettings) =
    processV1<CommentReadRequest, CommentReadResponse>(appSettings, clRead, "readComment")

private val clUpdate: KClass<*> = ApplicationCall::updateComment::class
suspend fun ApplicationCall.updateComment(appSettings: CommentsAppSettings) =
    processV1<CommentUpdateRequest, CommentUpdateResponse>(appSettings, clUpdate, "updateComment")

private val clDelete: KClass<*> = ApplicationCall::deleteComment::class
suspend fun ApplicationCall.deleteComment(appSettings: CommentsAppSettings) =
    processV1<CommentDeleteRequest, CommentDeleteResponse>(appSettings, clDelete, "deleteComment")

private val clSearch: KClass<*> = ApplicationCall::searchComment::class
suspend fun ApplicationCall.searchComment(appSettings: CommentsAppSettings) =
    processV1<CommentSearchRequest, CommentSearchResponse>(appSettings, clSearch, "searchComment")