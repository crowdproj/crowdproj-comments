import com.crowdproj.comments.common.CommentContext
import com.crowdproj.comments.common.exceptions.UnknownRequestException
import com.crowdproj.comments.common.models.*
import com.crowdproj.comments.common.stubs.CommentStubs
import com.crowdproj.product.comments.api.v1.models.*

fun CommentContext.fromTransport(request: IRequest) = when (request) {
    is CommentCreateRequest -> fromTransport(request)
    is CommentUpdateRequest -> fromTransport(request)
    is CommentDeleteRequest -> fromTransport(request)
    is CommentSearchRequest -> fromTransport(request)
    is CommentReadRequest -> fromTransport(request)
    else -> throw UnknownRequestException(request::class)
}

fun CommentContext.fromTransport(request: CommentCreateRequest) {
    command = CommentCommand.CREATE
    requestId = request.requestId()
    commentRequest = request.comment?.toInternal() ?: Comment()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun CommentContext.fromTransport(request: CommentUpdateRequest){
    command = CommentCommand.UPDATE
    requestId = request.requestId()
    commentRequest = request.comment?.toInternal() ?: Comment()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun CommentContext.fromTransport(request: CommentSearchRequest){
    command = CommentCommand.SEARCH
    requestId = request.requestId()
    commentFilterRequest = request.commentFilter?.toInternal() ?: CommentFilter.NONE
}

fun CommentContext.fromTransport(request: CommentDeleteRequest){
    command = CommentCommand.DELETE
    requestId = request.requestId()
    commentRequest = request.commentId.toCommentWithId()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun CommentContext.fromTransport(request: CommentReadRequest){
    command = CommentCommand.READ
    requestId = request.requestId()
    request.commentsIds?.forEach { commentsRequest.add(it.toCommentWithId()) }
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
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
    contentType = this.contentType.toInternal()
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

private fun ObjectType?.toInternal() = when (this){
    ObjectType.COMMENT -> CommentObjectType.COMMENT
    ObjectType.PRODUCT -> CommentObjectType.PRODUCT
    ObjectType.AD -> CommentObjectType.AD
    else -> CommentObjectType.NONE
}
private fun ICommentRequest?.requestId() = this?.requestId?.let{ CommentRequestId(it)} ?: CommentRequestId.NONE

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

private fun ContentType?.toInternal() = when(this) {
    ContentType.PLAIN -> CommentContentType.PLAIN
    ContentType.HTML -> CommentContentType.HTML
    ContentType.JSON -> CommentContentType.JSON
    else -> CommentContentType.NONE
}