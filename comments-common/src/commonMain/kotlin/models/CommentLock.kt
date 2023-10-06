package com.crowdproj.comments.common.models

import kotlin.jvm.JvmInline

@JvmInline
value class CommentLock(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = CommentLock("")
    }
}