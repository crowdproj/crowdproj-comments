package com.crowdproj.comments.common.models

data class CommentFilter(
    var objectType: CommentObjectType = CommentObjectType.NONE,
    var objectId: CommentObjectId = CommentObjectId.NONE,
    var userId: CommentUserId = CommentUserId.NONE,
    var searchPermissions: MutableSet<CommentSearchPermissions> = mutableSetOf()
) {
    companion object{
        var NONE = CommentFilter()
    }
}