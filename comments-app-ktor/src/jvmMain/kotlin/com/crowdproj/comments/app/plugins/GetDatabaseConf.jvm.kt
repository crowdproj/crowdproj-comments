package com.crowdproj.comments.app.plugins

import com.crowdproj.comments.app.cassandra.CassandraConfig
import com.crowdproj.comments.app.configs.ConfigPaths
import com.crowdproj.comments.common.repo.ICommentsRepository
import com.crowdproj.comments.repo.cassandra.CommentsRepoCassandra
import com.crowdproj.comments.repo.inmemory.CommentsRepoInMemory
import io.ktor.server.application.*
import io.ktor.server.config.*
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

actual fun Application.getDatabaseConf(type: CommentDbType): ICommentsRepository {
    val dbSettingPath = "${ConfigPaths.repository}.${type.confName}"
    val dbConfig = environment.config.config(dbSettingPath)
    val dbType = dbConfig.propertyOrNull("type")?.getString()?.lowercase() ?: throw NullPointerException(
        "$dbSettingPath.type must be set in application.yml"
    )

    return when(dbType) {
        "inmemory", "memory", "mem" -> initInMemory()
        "cassandra", "cass" -> initCassandra(dbConfig)
        else -> throw IllegalArgumentException(
            "$dbSettingPath.type must be set in application.yml to one of: " +
                    "'inmemory', 'cassandra'"
        )
    }
}


private fun Application.initCassandra(dbConfig: ApplicationConfig): ICommentsRepository {
    val config = CassandraConfig(dbConfig)
    return CommentsRepoCassandra(
        keyspaceName = config.keyspace,
        host = config.host,
        port = config.port,
        user = config.user,
        pass = config.pass,
    )
}

private fun Application.initInMemory(): ICommentsRepository {
    val ttlSetting = environment.config.propertyOrNull("db.prod")?.getString()?.let {
        Duration.parse(it)
    }
    return CommentsRepoInMemory(ttl = ttlSetting ?: 10.minutes)
}