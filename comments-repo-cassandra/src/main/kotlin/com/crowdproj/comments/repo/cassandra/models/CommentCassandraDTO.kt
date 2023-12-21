package com.crowdproj.comments.repo.cassandra.models

import com.crowdproj.comments.common.NONE
import com.crowdproj.comments.common.models.*
import com.datastax.oss.driver.api.core.type.DataTypes
import com.datastax.oss.driver.api.mapper.annotations.CqlName
import com.datastax.oss.driver.api.mapper.annotations.Entity
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder
import kotlinx.datetime.Instant
import kotlinx.datetime.toJavaInstant
import kotlinx.datetime.toKotlinInstant

@Entity
@CqlName(CommentCassandraDTO.TABLE_NAME)
data class CommentCassandraDTO(
    @CqlName(COLUMN_ID)
    @PartitionKey(1)
    val id: String? = null,

    @CqlName(COLUMN_OBJECT_TYPE)
    val objectType: CasCommentObjectType? = null,

    @CqlName(COLUMN_OBJECT_ID)
    val objectId: String? = null,

    @CqlName(COLUMN_USER_ID)
    val userId: String? = null,

    @CqlName(COLUMN_CONTENT)
    val content: String? = null,

    @CqlName(COLUMN_CONTENT_TYPE)
    val contentType: CasCommentContentType? = null,

    @CqlName(COLUMN_CREATED_AT)
    val createdAt: java.time.Instant? = null,

    @CqlName(COLUMN_UPDATED_AT)
    val updatedAt: java.time.Instant? = null,

    @CqlName(COLUMN_LOCK)
    val lock: String? = null
) {
    constructor(comment: Comment) : this(
        id = comment.id.takeIf { it != CommentId.NONE }?.asString(),
        objectType = comment.objectType.toTransport(),
        objectId = comment.objectId.takeIf { it != CommentObjectId.NONE }?.asString(),
        userId = comment.userId.takeIf { it != CommentUserId.NONE }?.asString(),
        content = comment.content,
        contentType = comment.contentType.toTransport(),
        createdAt = comment.createdAt.toJavaInstant(),
        updatedAt = comment.updatedAt.toJavaInstant(),
        lock = comment.lock.takeIf { it != CommentLock.NONE }?.asString(),
    )

    fun toCommentModel(): Comment = Comment(
        id = id?.let { CommentId(it) } ?: CommentId.NONE,
        objectType = objectType.fromTransport(),
        objectId = objectId?.let { CommentObjectId(it) } ?: CommentObjectId.NONE,
        userId = userId?.let { CommentUserId(it) } ?: CommentUserId.NONE,
        content = content ?: "",
        contentType = contentType.fromTransport(),
        createdAt = createdAt?.toKotlinInstant() ?: Instant.NONE,
        updatedAt = updatedAt?.toKotlinInstant() ?: Instant.NONE,
        lock = lock?.let { CommentLock(it) } ?: CommentLock.NONE
    )

    companion object {
        const val TABLE_NAME = "comments"

        const val COLUMN_ID = "id"
        const val COLUMN_OBJECT_TYPE = "object_type"
        const val COLUMN_OBJECT_ID = "object_id"
        const val COLUMN_USER_ID = "user_id"
        const val COLUMN_CONTENT = "content"
        const val COLUMN_CONTENT_TYPE = "content_type"
        const val COLUMN_CREATED_AT = "created_at"
        const val COLUMN_UPDATED_AT = "updated_at"
        const val COLUMN_LOCK = "lock"

        fun table(keyspace: String, tableName: String) =
            SchemaBuilder
                .createTable(keyspace, tableName)
                .ifNotExists()
                .withPartitionKey(COLUMN_ID, DataTypes.TEXT)
                .withColumn(COLUMN_OBJECT_TYPE, DataTypes.TEXT)
                .withColumn(COLUMN_OBJECT_ID, DataTypes.TEXT)
                .withColumn(COLUMN_USER_ID, DataTypes.TEXT)
                .withColumn(COLUMN_CONTENT, DataTypes.TEXT)
                .withColumn(COLUMN_CONTENT_TYPE, DataTypes.TEXT)
                .withColumn(COLUMN_CREATED_AT, DataTypes.TIMESTAMP)
                .withColumn(COLUMN_UPDATED_AT, DataTypes.TIMESTAMP)
                .withColumn(COLUMN_LOCK, DataTypes.TEXT)
                .build()

    }
}