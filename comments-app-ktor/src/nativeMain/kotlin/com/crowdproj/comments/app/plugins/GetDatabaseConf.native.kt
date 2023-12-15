package com.crowdproj.comments.app.plugins

import com.crowdproj.comments.common.repo.ICommentsRepository
import com.crowdproj.comments.repo.inmemory.CommentsRepoInMemory
import io.ktor.server.application.*

actual fun Application.getDatabaseConf(type: CommentDbType): ICommentsRepository {
    return CommentsRepoInMemory()
}