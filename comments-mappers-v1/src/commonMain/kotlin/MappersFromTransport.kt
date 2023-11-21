package com.crowdproj.comments.mappers.v1

import com.crowdproj.comments.common.CommentContext
import exceptions.UnknownRequestException
import com.crowdproj.comments.common.models.*
import com.crowdproj.comments.common.stubs.CommentStubs
import com.crowdproj.comments.api.v1.models.*

fun CommentContext.fromTransport(request: ICommentRequest) = when (request) {
    is CommentCreateRequest -> fromTransport(request)
    is CommentUpdateRequest -> fromTransport(request)
    is CommentDeleteRequest -> fromTransport(request)
    is CommentSearchRequest -> fromTransport(request)
    is CommentReadRequest -> fromTransport(request)
    else -> throw UnknownRequestException(request::class)
}.also {
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

private fun CommentContext.fromTransport(request: CommentCreateRequest) {
    command = CommentCommand.CREATE
    commentRequest = request.comment?.toInternal() ?: Comment()
}

private fun CommentContext.fromTransport(request: CommentReadRequest) {
    command = CommentCommand.READ
    request.comment?.id.toCommentId()
}

private fun CommentContext.fromTransport(request: CommentUpdateRequest) {
    command = CommentCommand.UPDATE
    commentRequest = request.comment?.toInternal() ?: Comment()
}

private fun CommentContext.fromTransport(request: CommentDeleteRequest) {
    command = CommentCommand.DELETE
    commentRequest = request.comment?.id.toCommentWithId().also {
        it.lock = request.comment?.lock.toCommentLock()
    }
}

private fun CommentContext.fromTransport(request: CommentSearchRequest) {
    command = CommentCommand.SEARCH
    commentFilterRequest = request.commentFilter?.toInternal() ?: CommentFilter.NONE
}


////////////////////////////////
private fun CommentCreateObject.toInternal(): Comment = Comment(
    objectType = this.objectType.toInternal(),
    objectId = this.objectId.toCommentObjectId(),
    userId = this.userId.toUserId(),
    content = this.content ?: "",
    contentType = this.contentType.toInternal()
)

private fun CommentUpdateObject.toInternal(): Comment = Comment(
    id = this.id.toCommentId(),
    objectType = this.objectType.toInternal(),
    objectId = this.objectId.toCommentObjectId(),
    userId = this.userId.toUserId(),
    content = this.content ?: "",
    contentType = this.contentType.toInternal(),
    lock = this.lock.toCommentLock()
)

fun CommentSearchFilter.toInternal(): CommentFilter = CommentFilter(
    objectType = this.objectType.toInternal(),
    objectId = this.objectId.toCommentObjectId(),
    userId = this.userId.toUserId()
)

private fun String?.toCommentId() = this?.let { CommentId(it) } ?: CommentId.NONE

private fun String?.toCommentObjectId() = this?.let { CommentObjectId(it) } ?: CommentObjectId.NONE

private fun String?.toUserId() = this?.let { CommentUserId(it) } ?: CommentUserId.NONE

private fun String?.toCommentWithId() = Comment(id = this.toCommentId())

private fun String?.toCommentLock() = this?.let { CommentLock(it) } ?: CommentLock.NONE

private fun List<String>?.toCommentsWithId() = this?.map { it.toCommentWithId() }

private fun ObjectType?.toInternal() = when (this) {
    ObjectType.COMMENT -> CommentObjectType.COMMENT
    ObjectType.PRODUCT -> CommentObjectType.PRODUCT
    ObjectType.AD -> CommentObjectType.AD
    else -> CommentObjectType.NONE
}

private fun CpBaseDebug?.transportToWorkMode(): CommentWorkMode = when (this?.mode) {
    CpRequestDebugMode.PROD -> CommentWorkMode.PROD
    CpRequestDebugMode.TEST -> CommentWorkMode.TEST
    CpRequestDebugMode.STUB -> CommentWorkMode.STUB
    null -> CommentWorkMode.PROD
}

private fun CpBaseDebug?.transportToStubCase(): CommentStubs = when (this?.stub) {
    CpRequestDebugStubs.SUCCESS -> CommentStubs.SUCCESS
    CpRequestDebugStubs.NOT_FOUND -> CommentStubs.NOT_FOUND
    CpRequestDebugStubs.BAD_ID -> CommentStubs.BAD_ID
    null -> CommentStubs.NONE
}

private fun ContentType?.toInternal() = when (this) {
    ContentType.PLAIN -> CommentContentType.PLAIN
    ContentType.HTML -> CommentContentType.HTML
    ContentType.JSON -> CommentContentType.JSON
    else -> CommentContentType.NONE
}