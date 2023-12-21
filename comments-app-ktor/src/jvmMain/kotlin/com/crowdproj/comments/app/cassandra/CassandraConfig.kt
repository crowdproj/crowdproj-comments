package com.crowdproj.comments.app.cassandra

import io.ktor.server.config.*

data class CassandraConfig(
    val host: String = "localhost",
    val port: Int = 9042,
    val user: String = "cassandra",
    val pass: String = "cassandra",
    val keyspace: String = "test_keyspace"
) {
    constructor(config: ApplicationConfig) : this(
        host = config.property("host").getString(),
        port = config.property("port").getString().toInt(),
        user = config.property("user").getString(),
        pass = config.property("pass").getString(),
        keyspace = config.property("keyspace").getString()
    )
}