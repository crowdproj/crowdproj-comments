package com.crowdproj.comments.biz

import com.crowdproj.comments.biz.group.operation
import com.crowdproj.comments.biz.group.stub
import com.crowdproj.comments.biz.workers.*
import com.crowdproj.comments.biz.workers.stubs.*
import com.crowdproj.comments.common.CommentContext
import com.crowdproj.comments.common.config.CommentsCorSettings
import com.crowdproj.comments.common.models.*
import com.crowdproj.kotlin.cor.rootChain

class CommentProcessor(
    @Suppress("unused")
    private val corSettings: CommentsCorSettings = CommentsCorSettings.NONE
) {
    suspend fun exec(ctx: CommentContext) = BusinessChain.exec(ctx)

    companion object {
        val BusinessChain = rootChain {
            initChain("Init business chain")
            operation("Create", CommentCommand.CREATE) {
                stub("Stub processing") {
                    stubCreateSuccess("Simulating successful create processing")
                    stubDbError("Simulating db error")
                    stubNoCase("Error: wrong stub case")
                }
            }
            operation("Read", CommentCommand.READ) {
                stub("Stub processing") {
                    stubReadSuccess("Simulating successful read processing")
                    stubValidationBadId("Simulating incorrect id")
                    stubNotFound("Simulating not found")
                    stubDbError("Simulating db error")
                    stubNoCase("Error: wrong stub case")
                }
            }
            operation("Update", CommentCommand.UPDATE) {
                stub("Stub processing") {
                    stubUpdateSuccess("Simulating successful update processing")
                    stubValidationBadId("Simulating incorrect id")
                    stubNotFound("Simulating not found")
                    stubDbError("Simulating db error")
                    stubNoCase("Error: wrong stub case")
                }
            }
            operation("Delete", CommentCommand.DELETE) {
                stub("Stub processing") {
                    stubDeleteSuccess("Simulating successful delete processing")
                    stubValidationBadId("Simulating incorrect id")
                    stubNotFound("Simulating not found")
                    stubDbError("Simulating db error")
                    stubNoCase("Error: wrong stub case")
                }
            }
            operation("Search", CommentCommand.SEARCH) {
                stub("Stub processing") {
                    stubSearchSuccess("Simulating successful search processing")
                    stubValidationBadId("Simulating incorrect id")
                    stubNotFound("Simulating not found")
                    stubDbError("Simulating db error")
                    stubNoCase("Error: wrong stub case")
                }
            }
        }.build()
    }
}

