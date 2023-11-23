package com.crowdproj.comments.app.controllers

import com.crowdproj.comments.api.v1.models.*
import com.crowdproj.comments.app.configs.CommentsAppSettings
import com.crowdproj.comments.app.helpers.processV1
import io.ktor.server.application.*
import kotlin.reflect.KClass

private val clCreate: KClass<*> = ApplicationCall::createAd::class
suspend fun ApplicationCall.createAd(appSettings: CommentsAppSettings) =
    processV1<CommentCreateRequest, CommentCreateResponse>(appSettings, clCreate, "createComment")

private val clRead: KClass<*> = ApplicationCall::readAd::class
suspend fun ApplicationCall.readAd(appSettings: CommentsAppSettings) =
    processV1<CommentReadRequest, CommentReadResponse>(appSettings, clRead, "readComment")

private val clUpdate: KClass<*> = ApplicationCall::updateAd::class
suspend fun ApplicationCall.updateAd(appSettings: CommentsAppSettings) =
    processV1<CommentUpdateRequest, CommentUpdateResponse>(appSettings, clUpdate, "updateComment")

private val clDelete: KClass<*> = ApplicationCall::deleteAd::class
suspend fun ApplicationCall.deleteAd(appSettings: CommentsAppSettings) =
    processV1<CommentDeleteRequest, CommentDeleteResponse>(appSettings, clDelete, "deleteComment")

private val clSearch: KClass<*> = ApplicationCall::searchAd::class
suspend fun ApplicationCall.searchAd(appSettings: CommentsAppSettings) =
    processV1<CommentSearchRequest, CommentSearchResponse>(appSettings, clSearch, "searchComment")