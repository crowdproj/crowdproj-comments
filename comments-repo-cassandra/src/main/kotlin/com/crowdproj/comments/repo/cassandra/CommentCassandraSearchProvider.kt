package com.crowdproj.comments.repo.cassandra

import com.crowdproj.comments.common.models.CommentObjectId
import com.crowdproj.comments.common.models.CommentObjectType
import com.crowdproj.comments.common.models.CommentUserId
import com.crowdproj.comments.common.repo.DbCommentFilterRequest
import com.crowdproj.comments.repo.cassandra.models.CommentCassandraDTO
import com.crowdproj.comments.repo.cassandra.models.toTransport
import com.datastax.oss.driver.api.core.cql.AsyncResultSet
import com.datastax.oss.driver.api.mapper.MapperContext
import com.datastax.oss.driver.api.mapper.entity.EntityHelper
import com.datastax.oss.driver.api.querybuilder.QueryBuilder
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletionStage
import java.util.function.BiConsumer

class CommentCassandraSearchProvider(
    private val context: MapperContext,
    private val entityHelper: EntityHelper<CommentCassandraDTO>
) {

    fun search(filter: DbCommentFilterRequest): CompletionStage<Collection<CommentCassandraDTO>> {
        var select = entityHelper.selectStart().allowFiltering()

        if (filter.objectType != CommentObjectType.NONE) {
            select = select
                .whereColumn(CommentCassandraDTO.COLUMN_OBJECT_TYPE)
                .isEqualTo(QueryBuilder.literal(filter.objectType.toTransport(), context.session.context.codecRegistry))
        }
        if(filter.objectId != CommentObjectId.NONE) {
            select = select
                .whereColumn(CommentCassandraDTO.COLUMN_OBJECT_ID)
                .isEqualTo(QueryBuilder.literal(filter.objectId.asString(), context.session.context.codecRegistry))
        }
        if(filter.userId != CommentUserId.NONE) {
            select = select
                .whereColumn(CommentCassandraDTO.COLUMN_USER_ID)
                .isEqualTo(QueryBuilder.literal(filter.userId.asString(), context.session.context.codecRegistry))
        }

        val asyncFetcher = AsyncFetcher()

        context.session
            .executeAsync(select.build())
            .whenComplete(asyncFetcher)

        return asyncFetcher.stage
    }

    inner class AsyncFetcher : BiConsumer<AsyncResultSet?, Throwable?> {
        private val buffer = mutableListOf<CommentCassandraDTO>()
        private val future = CompletableFuture<Collection<CommentCassandraDTO>>()
        val stage: CompletionStage<Collection<CommentCassandraDTO>> = future

        override fun accept(resultSet: AsyncResultSet?, t: Throwable?) {
            when {
                t != null -> future.completeExceptionally(t)
                resultSet == null -> future.completeExceptionally(IllegalStateException("ResultSet should not be null"))
                else -> {
                    buffer.addAll(resultSet.currentPage().map { entityHelper.get(it, false) })
                    if (resultSet.hasMorePages())
                        resultSet.fetchNextPage().whenComplete(this)
                    else
                        future.complete(buffer)
                }
            }
        }
    }

}


