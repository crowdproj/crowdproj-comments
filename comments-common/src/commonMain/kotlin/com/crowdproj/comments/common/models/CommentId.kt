package com.crowdproj.comments.common.models

data class CommentId(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = CommentId("")
    }
}