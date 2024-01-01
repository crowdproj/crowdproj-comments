package com.crowdproj.comments.biz.permissions

import com.crowdproj.comments.auth.checkPermitted
import com.crowdproj.comments.auth.resolveRelationsTo
import com.crowdproj.comments.common.CommentContext
import com.crowdproj.comments.common.helpers.fail
import com.crowdproj.comments.common.models.CommentError
import com.crowdproj.comments.common.models.CommentState
import com.crowdproj.kotlin.cor.handlers.CorChainDsl
import com.crowdproj.kotlin.cor.handlers.chain
import com.crowdproj.kotlin.cor.handlers.worker

fun CorChainDsl<CommentContext>.accessValidation(title: String) = chain {
    this.title = title
    description = "Resolve permissions by user groups and access table"
    on { state == CommentState.RUNNING && settings.authEnabled }
    worker("Resolve principal relations") {
        commentRepoRead.principalRelations = commentRepoRead.resolveRelationsTo(principal)
    }
    worker("Validate access") {
        permitted = checkPermitted(command, commentRepoRead.principalRelations, permissionsChain)
    }
    worker{
        this.title = "Check access"
        description = "Check access for command"
        on { !permitted }
        handle {
            fail(
                CommentError("User is not permitted to do this action")
            )
        }
    }
}