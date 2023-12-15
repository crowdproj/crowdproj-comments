package com.crowdproj.comments.app.plugins

import com.crowdproj.comments.common.repo.ICommentsRepository
import com.crowdproj.comments.repo.cassandra.CommentsRepoCassandra
import io.ktor.server.application.*

actual fun Application.getDatabaseConf(type: CommentDbType): ICommentsRepository {
    return CommentsRepoCassandra(
        keyspaceName = "comments",
        host = "127.0.0.1",
        testing = true,
    )
}