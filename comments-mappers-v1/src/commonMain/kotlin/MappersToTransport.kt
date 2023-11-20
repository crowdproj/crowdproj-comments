import com.crowdproj.comments.common.CommentContext
import com.crowdproj.comments.common.NONE
import com.crowdproj.comments.common.models.*
import com.crowdproj.comments.api.v1.models.*
import exceptions.UnknownCommandException
import kotlinx.datetime.Instant

fun CommentContext.toTransport(): IResponse = when (command) {
    CommentCommand.CREATE -> toTransportCreate()
    CommentCommand.READ -> toTransportRead()
    CommentCommand.UPDATE -> toTransportUpdate()
    CommentCommand.DELETE -> toTransportDelete()
    CommentCommand.SEARCH -> toTransportSearch()
    CommentCommand.INIT -> toTransportInit()
    CommentCommand.FINISH -> throw UnknownCommandException(command)
    CommentCommand.NONE -> throw UnknownCommandException(command)
}

private fun CommentContext.toTransportInit() = CommentInitResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (errors.isEmpty()) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors()
)

private fun CommentContext.toTransportCreate() = CommentCreateResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = state.toTransport(),
    errors = errors.toTransportErrors(),
    comment = commentResponse.toTransportComment()
)

private fun CommentContext.toTransportRead() = CommentReadResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = state.toTransport(),
    errors = errors.toTransportErrors(),
    comment = commentResponse.toTransportComment()
)

private fun CommentContext.toTransportUpdate() = CommentUpdateResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = state.toTransport(),
    errors = errors.toTransportErrors(),
    comment = commentResponse.toTransportComment()
)

private fun CommentContext.toTransportDelete() = CommentDeleteResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = state.toTransport(),
    errors = errors.toTransportErrors(),
    comment = commentResponse.toTransportComment()
)

private fun CommentContext.toTransportSearch() = CommentSearchResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = state.toTransport(),
    errors = errors.toTransportErrors(),
    comments = commentsResponse.toTransportComments()
)

private fun MutableList<CommentError>.toTransportErrors() = this
    .map { it.toTransportError() }
    .toList()
    .takeIf { it.isNotEmpty() }

private fun CommentError.toTransportError() = Error(
    code = code.takeIf { it.isNotBlank() },
    group = group.takeIf { it.isNotBlank() },
    field = field.takeIf { it.isNotBlank() },
    title = message.takeIf { it.isNotBlank() },
    description = null
)

private fun MutableList<Comment>.toTransportComments() = this
    .map { it.toTransportComment() }
    .toList()
    .takeIf { it.isNotEmpty() }

private fun Comment.toTransportComment() = CommentResponseObject(
    id = id.takeIf { it != CommentId.NONE }?.asString(),
    createdAt = createdAt.takeIf { it != Instant.NONE }?.toString(),
    updatedAt = updatedAt.takeIf { it != Instant.NONE }?.toString(),
    objectType = objectType.toTransport(),
    objectId = objectId.takeIf { it != CommentObjectId.NONE }?.asString(),
    userId = userId.takeIf { it != CommentUserId.NONE }?.asString(),
    content = content.takeIf { it.isNotBlank() },
    contentType = contentType.toTransport(),
    lock = lock.takeIf { it != CommentLock.NONE }?.asString()
)

private fun CommentObjectType.toTransport() = when (this) {
    CommentObjectType.COMMENT -> ObjectType.COMMENT
    CommentObjectType.PRODUCT -> ObjectType.PRODUCT
    CommentObjectType.AD -> ObjectType.AD
    CommentObjectType.NONE -> null
}

private fun CommentContentType.toTransport() = when (this) {
    CommentContentType.PLAIN -> ContentType.PLAIN
    CommentContentType.HTML -> ContentType.HTML
    CommentContentType.JSON -> ContentType.JSON
    CommentContentType.NONE -> null
}

private fun CommentState.toTransport() = when (this) {
    CommentState.NONE -> null
    CommentState.RUNNING -> ResponseResult.SUCCESS
    CommentState.FAILING -> ResponseResult.ERROR
    CommentState.FINISHING -> ResponseResult.SUCCESS
}