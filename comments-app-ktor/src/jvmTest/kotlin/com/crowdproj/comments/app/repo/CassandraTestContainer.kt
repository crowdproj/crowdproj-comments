package com.crowdproj.comments.app.repo

import com.crowdproj.comments.common.models.Comment
import com.crowdproj.comments.repo.cassandra.CommentsRepoCassandra
import org.testcontainers.containers.CassandraContainer
import java.time.Duration

class CasandraTestContainer : CassandraContainer<CasandraTestContainer>("cassandra:3.11.16") {
    companion object {
        private val container by lazy {
            CasandraTestContainer().withStartupTimeout(Duration.ofSeconds(300L))
                .also { it.start() }
        }

        fun repository(initObjects: List<Comment>, keyspace: String): CommentsRepoCassandra {
            return CommentsRepoCassandra(
                keyspaceName = keyspace,
                host = container.host,
                port = container.getMappedPort(CQL_PORT),
                testing = true, initObjects = initObjects
            )
        }
    }
}