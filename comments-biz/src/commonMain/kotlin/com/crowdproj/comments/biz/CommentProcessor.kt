package com.crowdproj.comments.biz

import com.crowdproj.comments.common.CommentContext
import com.crowdproj.comments.common.config.CommentsCorSettings
import com.crowdproj.comments.common.models.CommentCommand
import com.crowdproj.comments.common.models.CommentObjectType
import com.crowdproj.comments.common.models.CommentState
import com.crowdproj.comments.common.models.CommentWorkMode
import com.crowdproj.comments.stubs.CommentStub

class CommentProcessor(
    @Suppress("unused")
    private val settings: CommentsCorSettings = CommentsCorSettings.NONE
) {
    suspend fun exec(ctx: CommentContext){
        //TODO: Rewrite temporary stub solution with BIZ
        require(ctx.workMode == CommentWorkMode.STUB || ctx.command in arrayOf(CommentCommand.INIT, CommentCommand.FINISH)){
            "Currently working only in STUB mode."
        }

        if (ctx.state == CommentState.NONE) ctx.state = CommentState.RUNNING
        when (ctx.command) {
            CommentCommand.SEARCH -> {
                ctx.commentsResponse.addAll(CommentStub.prepareSearchList("d-666-01", CommentObjectType.PRODUCT))
            }
            else -> {
                ctx.commentResponse = CommentStub.get()
            }
        }
    }
}