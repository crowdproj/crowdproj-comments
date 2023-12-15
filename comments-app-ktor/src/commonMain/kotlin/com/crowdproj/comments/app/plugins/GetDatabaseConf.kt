package com.crowdproj.comments.app.plugins

import com.crowdproj.comments.common.repo.ICommentsRepository
import io.ktor.server.application.*

expect fun Application.getDatabaseConf(type: CommentDbType): ICommentsRepository

enum class CommentDbType(val confName: String) {
    PROD("prod"), TEST("test")
}