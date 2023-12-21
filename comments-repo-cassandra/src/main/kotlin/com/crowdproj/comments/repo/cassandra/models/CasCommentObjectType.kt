package com.crowdproj.comments.repo.cassandra.models

import com.crowdproj.comments.common.models.CommentObjectType

enum class CasCommentObjectType {
    AD,
    PRODUCT,
    COMMENT,
}

fun CasCommentObjectType?.fromTransport() = when (this) {
    CasCommentObjectType.AD -> CommentObjectType.AD
    CasCommentObjectType.PRODUCT -> CommentObjectType.PRODUCT
    CasCommentObjectType.COMMENT -> CommentObjectType.COMMENT
    null -> CommentObjectType.NONE
}

fun CommentObjectType.toTransport() = when (this) {
    CommentObjectType.AD -> CasCommentObjectType.AD
    CommentObjectType.PRODUCT -> CasCommentObjectType.PRODUCT
    CommentObjectType.COMMENT -> CasCommentObjectType.COMMENT
    CommentObjectType.NONE -> null
}