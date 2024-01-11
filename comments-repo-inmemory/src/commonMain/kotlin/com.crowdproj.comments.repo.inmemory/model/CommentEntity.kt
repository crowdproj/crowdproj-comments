package com.crowdproj.comments.repo.inmemory.model

import com.crowdproj.comments.common.NONE
import com.crowdproj.comments.common.models.*
import kotlinx.datetime.Instant

data class CommentEntity(
    val id: String? = null,
    val objectType: String? = null,
    val objectId: String? = null,
    val userId: String? = null,
    val content: String? = null,
    val contentType: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null,
    val lock: String? = null,
    val permissionsClient: String? = null
) {
    constructor(model: Comment): this(
        id = model.id.asString().takeIf { it.isNotBlank() },
        objectType = model.objectType.takeIf { it != CommentObjectType.NONE }?.name,
        objectId = model.objectId.asString().takeIf { it.isNotBlank() },
        userId = model.userId.asString().takeIf { it.isNotBlank() },
        content = model.content.takeIf { it.isNotBlank() },
        contentType = model.contentType.takeIf { it != CommentContentType.NONE }?.name,
        createdAt = model.createdAt.takeIf { it != Instant.NONE }?.toString(),
        updatedAt = model.updatedAt.takeIf { it != Instant.NONE }?.toString(),
        lock = model.lock.takeIf { it != CommentLock.NONE }?.asString(),
        permissionsClient = model.permissionsClient.takeIf { it.isNotEmpty() }?.joinToString(";")
    )

    fun toInternal() = Comment(
        id = id?.let { CommentId(it) } ?: CommentId.NONE,
        objectType = objectType?.let { CommentObjectType.valueOf(it) } ?: CommentObjectType.NONE,
        objectId = objectId?.let { CommentObjectId(it) } ?: CommentObjectId.NONE,
        userId = userId?.let { CommentUserId(it) } ?: CommentUserId.NONE,
        content = content ?: "",
        contentType = contentType?.let { CommentContentType.valueOf(it) } ?: CommentContentType.NONE,
        createdAt = createdAt?.let { Instant.parse(it) } ?: Instant.NONE,
        updatedAt = updatedAt?.let { Instant.parse(it) } ?: Instant.NONE,
        lock = lock?.let { CommentLock(it) } ?: CommentLock.NONE,
        permissionsClient = permissionsClient?.split(";")?.map { CommentPermissionClient.valueOf(it) }?.toMutableSet() ?: mutableSetOf()
    )
}