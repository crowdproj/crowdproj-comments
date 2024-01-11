package com.crowdproj.comments.mappers.log

import com.crowdproj.comments.api.logs.models.*
import com.crowdproj.comments.common.CommentContext
import com.crowdproj.comments.common.models.*
import kotlinx.datetime.Clock

fun CommentContext.toLog(logId: String) = CommonLogModel(
    messageTime = Clock.System.now().toString(),
    logId = logId,
    source = "crowdproj-comments",
    comment = toCommentLog(),
    errors = errors.map { it.toLog() }
)

fun CommentContext.toCommentLog(): CommentLogModel? {
    val commentNone = Comment()
    return CommentLogModel(
        requestId = requestId.takeIf { it != CommentRequestId.NONE }?.toString(),
        operation = command.toLogModel(),
        requestComment = commentRequest.takeIf { it != commentNone }?.toLog(),
        requestsComment = commentsRequest.takeIf { it.isNotEmpty() }?.filter { it != commentNone }?.map { it.toLog() },
        requestFilter = commentFilterRequest.takeIf { it != CommentFilter() }?.toLog(),
        responseComment = commentResponse.takeIf { it != commentNone }?.toLog(),
        responseComments = commentsResponse.takeIf { it.isNotEmpty() }?.filter { it != commentNone }?.map { it.toLog() }
    ).takeIf { it != CommentLogModel() }
}

private fun CommentCommand.toLogModel(): CommentLogModel.Operation? = when(this) {
    CommentCommand.CREATE -> CommentLogModel.Operation.CREATE
    CommentCommand.READ -> CommentLogModel.Operation.READ
    CommentCommand.UPDATE -> CommentLogModel.Operation.UPDATE
    CommentCommand.DELETE -> CommentLogModel.Operation.DELETE
    CommentCommand.SEARCH -> CommentLogModel.Operation.SEARCH
    CommentCommand.INIT -> CommentLogModel.Operation.INIT
    CommentCommand.FINISH -> CommentLogModel.Operation.FINISH
    CommentCommand.NONE -> null
}

private fun CommentFilter.toLog() = CommentFilterLog(
    objectType = objectType.takeIf { it != CommentObjectType.NONE }?.name,
    objectId = objectId.takeIf { it != CommentObjectId.NONE }?.asString(),
    userId = userId.takeIf { it != CommentUserId.NONE }?.asString()
)

fun CommentError.toLog() = ErrorLogModel(
    message = message.takeIf { it.isNotBlank() },
    field = field.takeIf { it.isNotBlank() },
    code = code.takeIf { it.isNotBlank() },
    level = level.name,
)

fun Comment.toLog() = CommentLog(
    id = id.takeIf { it != CommentId.NONE }?.asString(),
    objectType = objectType.takeIf { it != CommentObjectType.NONE }?.name,
    objectId = objectId.takeIf { it != CommentObjectId.NONE }?.asString(),
    userId = userId.takeIf { it != CommentUserId.NONE }?.asString(),
    contentType = contentType.takeIf { it != CommentContentType.NONE }?.name,
    permissions = permissionsClient.takeIf { it.isNotEmpty() }?.filter { it.name.isNotBlank() }?.map { it.name }?.toSet()
)