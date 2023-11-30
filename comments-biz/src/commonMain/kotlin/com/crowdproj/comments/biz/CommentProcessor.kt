package com.crowdproj.comments.biz

import com.crowdproj.comments.biz.group.operation
import com.crowdproj.comments.biz.group.stub
import com.crowdproj.comments.biz.validation.*
import com.crowdproj.comments.biz.workers.*
import com.crowdproj.comments.biz.workers.stubs.*
import com.crowdproj.comments.common.CommentContext
import com.crowdproj.comments.common.config.CommentsCorSettings
import com.crowdproj.comments.common.models.*
import com.crowdproj.kotlin.cor.handlers.worker
import com.crowdproj.kotlin.cor.rootChain

class CommentProcessor(
    @Suppress("unused")
    private val corSettings: CommentsCorSettings = CommentsCorSettings.NONE
) {
    suspend fun exec(ctx: CommentContext) = BusinessChain.exec(ctx)

    companion object {
        val BusinessChain = rootChain {
            initChain("Init business chain")
            operation("Create comment", CommentCommand.CREATE) {
                stub("Stub processing") {
                    stubCreateSuccess("Simulating successful create processing")
                    stubDbError("Simulating db error")
                    stubNoCase("Error: wrong stub case")
                }
                validation {
                    worker("Copy fields to commentsValidating")
                    { commentValidating = commentRequest.deepCopy() }
                    worker("Cleaning id")
                    { commentValidating.id = CommentId.NONE }
                    worker("Clean object id")
                    { commentValidating.objectId = CommentObjectId(commentValidating.objectId.asString().trim()) }
                    worker("Clean user id")
                    { commentValidating.userId = CommentUserId(commentValidating.userId.asString().trim()) }
                    worker("Clean content")
                    { commentValidating.content = commentValidating.content.trim() }

                    validateObjectIdNotEmpty("Validate objectId is not empty")
                    validateObjectIdProperFormat("Validate objectId is proper format")
                    validateUserIdNotEmpty("Validate userId is not empty")
                    validateUserIdProperFormat("Validate userId is proper format")
                    validateContentNotEmpty("Validate content is not empty")
                    validateContentPlainAcceptable("Validate content is acceptable")
                    validateContentType("Validate content type")

                    finishCommentValidation("Finish validation")
                }
            }
            operation("Read comment", CommentCommand.READ) {
                stub("Stub processing") {
                    stubReadSuccess("Simulating successful read processing")
                    stubValidationBadId("Simulating incorrect id")
                    stubNotFound("Simulating not found")
                    stubDbError("Simulating db error")
                    stubNoCase("Error: wrong stub case")
                }
                validation {
                    worker("Copy fields to commentsValidating")
                    { commentValidating = commentRequest.deepCopy() }
                    worker("Cleaning id")
                    { commentValidating.id = CommentId(commentValidating.id.asString().trim()) }
                    validateCommentIdNotEmpty("Validate id is not empty")
                    validateCommentIdProperFormat("Validate id is proper format")

                    finishCommentValidation("Finish validation")
                }
            }
            operation("Update comment", CommentCommand.UPDATE) {
                stub("Stub processing") {
                    stubUpdateSuccess("Simulating successful update processing")
                    stubValidationBadId("Simulating incorrect id")
                    stubNotFound("Simulating not found")
                    stubDbError("Simulating db error")
                    stubNoCase("Error: wrong stub case")
                }
                validation {
                    worker("Copy fields to commentsValidating")
                    { commentValidating = commentRequest.deepCopy() }
                    worker("Cleaning id")
                    { commentValidating.id = CommentId(commentValidating.id.asString().trim()) }
                    worker("Clean object id")
                    { commentValidating.objectId = CommentObjectId(commentValidating.objectId.asString().trim()) }
                    worker("Clean user id")
                    { commentValidating.userId = CommentUserId(commentValidating.userId.asString().trim()) }
                    worker("Clean content")
                    { commentValidating.content = commentValidating.content.trim() }
                    validateCommentIdNotEmpty("Validate id is not empty")
                    validateCommentIdProperFormat("Validate id is proper format")
                    validateObjectIdNotEmpty("Validate objectId is not empty")
                    validateObjectIdProperFormat("Validate objectId is proper format")
                    validateUserIdNotEmpty("Validate userId is not empty")
                    validateUserIdProperFormat("Validate userId is proper format")
                    validateContentNotEmpty("Validate content is not empty")
                    validateContentPlainAcceptable("Validate content is acceptable")
                    validateContentType("Validate content type")

                    finishCommentValidation("Finish validation")
                }
            }
            operation("Delete comment", CommentCommand.DELETE) {
                stub("Stub processing") {
                    stubDeleteSuccess("Simulating successful delete processing")
                    stubValidationBadId("Simulating incorrect id")
                    stubNotFound("Simulating not found")
                    stubDbError("Simulating db error")
                    stubNoCase("Error: wrong stub case")
                }
                validation {
                    worker("Copy fields to commentsValidating")
                    { commentValidating = commentRequest.deepCopy() }
                    worker("Cleaning id")
                    { commentValidating.id = CommentId(commentValidating.id.asString().trim()) }
                    validateCommentIdNotEmpty("Validate id is not empty")
                    validateCommentIdProperFormat("Validate id is proper format")

                    finishCommentValidation("Finish validation")
                }
            }
            operation("Search comments", CommentCommand.SEARCH) {
                stub("Stub processing") {
                    stubSearchSuccess("Simulating successful search processing")
                    stubValidationBadId("Simulating incorrect id")
                    stubNotFound("Simulating not found")
                    stubDbError("Simulating db error")
                    stubNoCase("Error: wrong stub case")
                }
                validation {
                    worker("Copy fields to commentsValidating")
                    { commentFilterValidating = commentFilterRequest.copy() }
                    worker("Clean object id")
                    { commentFilterValidating.objectId = CommentObjectId(commentFilterValidating.objectId.asString().trim()) }
                    worker("Clean user id")
                    { commentFilterValidating.userId = CommentUserId(commentFilterValidating.userId.asString().trim()) }
                    validateCommentFilter("Validate filter")

                    finishCommentFilterValidation("Finish validation")
                }
            }
        }.build()
    }
}

