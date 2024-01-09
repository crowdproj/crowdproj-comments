package com.crowdproj.comments.biz.permissions

import com.crowdproj.comments.common.CommentContext
import com.crowdproj.comments.common.models.CommentSearchPermissions
import com.crowdproj.comments.common.models.CommentState
import com.crowdproj.comments.common.permissions.CommentsUserPermissions
import com.crowdproj.kotlin.cor.handlers.CorChainDsl
import com.crowdproj.kotlin.cor.handlers.chain
import com.crowdproj.kotlin.cor.handlers.worker

fun CorChainDsl<CommentContext>.searchTypes(title: String) = chain {
    this.title = title
    description = "Adding restricts to search request by access rights"
    on { state == CommentState.RUNNING }
    worker("Add restricts to search filter") {
        commentFilterValidated.searchPermissions = setOfNotNull(
            CommentSearchPermissions.OWN.takeIf { permissionsChain.contains(CommentsUserPermissions.SEARCH_OWN) },
            CommentSearchPermissions.PUBLIC.takeIf { permissionsChain.contains(CommentsUserPermissions.SEARCH_PUBLIC) },
        ).toMutableSet()
    }
}