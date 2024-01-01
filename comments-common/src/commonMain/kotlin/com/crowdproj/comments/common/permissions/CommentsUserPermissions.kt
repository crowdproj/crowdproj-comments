package com.crowdproj.comments.common.permissions

enum class CommentsUserPermissions {
    CREATE_OWN,

    READ_OWN,
    READ_GROUP,
    READ_PUBLIC,

    UPDATE_OWN,
    UPDATE_PUBLIC,

    DELETE_OWN,
    DELETE_PUBLIC,

    SEARCH_OWN,
    SEARCH_PUBLIC,
}