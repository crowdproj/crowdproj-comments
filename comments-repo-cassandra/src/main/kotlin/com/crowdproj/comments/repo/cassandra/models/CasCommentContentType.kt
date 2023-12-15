package com.crowdproj.comments.repo.cassandra.models

import com.crowdproj.comments.common.models.CommentContentType

enum class CasCommentContentType {
    PLAIN,
    HTML,
    JSON,
}

fun CasCommentContentType?.fromTransport() = when (this) {
    CasCommentContentType.PLAIN -> CommentContentType.PLAIN
    CasCommentContentType.HTML -> CommentContentType.HTML
    CasCommentContentType.JSON -> CommentContentType.JSON
    null -> CommentContentType.NONE
}

fun CommentContentType.toTransport() = when (this) {
    CommentContentType.PLAIN -> CasCommentContentType.PLAIN
    CommentContentType.HTML -> CasCommentContentType.HTML
    CommentContentType.JSON -> CasCommentContentType.JSON
    CommentContentType.NONE -> null
}