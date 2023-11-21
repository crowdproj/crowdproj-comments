package com.crowdproj.comments.common.models

data class CommentFilter(
    var objectType: CommentObjectType = CommentObjectType.NONE,
    var objectId: CommentObjectId = CommentObjectId.NONE,
    var userId: CommentUserId = CommentUserId.NONE
) {
    companion object{
        var NONE = CommentFilter()
    }
}