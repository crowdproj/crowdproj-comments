package com.crowdproj.comments.biz.permissions

import com.crowdproj.comments.auth.resolveChainPermissions
import com.crowdproj.comments.common.CommentContext
import com.crowdproj.comments.common.models.CommentState
import com.crowdproj.kotlin.cor.handlers.CorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker

fun CorChainDsl<CommentContext>.chainPermissions(title: String) = worker {
    this.title = title
    description = "Calculating permissions for user groups"

    on { state == CommentState.RUNNING }

    handle {
        permissionsChain.addAll(resolveChainPermissions(principal.groups))
        println("PRINCIPAL: $principal")
        println("PERMISSIONS: $permissionsChain")
    }
}