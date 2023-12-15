package com.crowdproj.comments.repo.cassandra

import com.crowdproj.comments.common.repo.DbCommentFilterRequest
import com.crowdproj.comments.repo.cassandra.models.CommentCassandraDTO
import com.datastax.oss.driver.api.core.cql.AsyncResultSet
import com.datastax.oss.driver.api.mapper.annotations.*
import java.util.concurrent.CompletionStage

@Dao
interface CommentsCassandraDAO {
    @Insert
    fun create(dto: CommentCassandraDTO): CompletionStage<Unit>

    @Select
    fun read(id: String): CompletionStage<CommentCassandraDTO?>

    @Update(customIfClause = "lock = :prevLock")
    fun update(dto: CommentCassandraDTO, prevLock: String): CompletionStage<AsyncResultSet>

    @Delete(customWhereClause = "id = :id", customIfClause = "lock = :prevLock", entityClass = [CommentCassandraDTO::class])
    fun delete(id: String, prevLock: String): CompletionStage<AsyncResultSet>

    @QueryProvider(providerClass = CommentCassandraSearchProvider::class, entityHelpers = [CommentCassandraDTO::class])
    fun search(filter: DbCommentFilterRequest): CompletionStage<Collection<CommentCassandraDTO>>

}