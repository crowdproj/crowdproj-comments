package com.crowdproj.comments.biz.permissions

import com.crowdproj.comments.auth.resolveFrontPermissions
import com.crowdproj.comments.auth.resolveRelationsTo
import com.crowdproj.comments.common.CommentContext
import com.crowdproj.comments.common.models.CommentState
import com.crowdproj.kotlin.cor.handlers.CorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker

fun CorChainDsl<CommentContext>.frontPermissions(title: String) = worker {
    this.title = title
    description = "Resolving permissions for user front"

    on { state == CommentState.RUNNING && settings.authEnabled}

    handle {
        commentRepoDone.permissionsClient.addAll(
            resolveFrontPermissions(
                permissionsChain,
                // Повторно вычисляем отношения, поскольку они могли измениться при выполении операции
                commentRepoDone.resolveRelationsTo(principal)
            )
        )

        for (ad in commentsRepoDone) {
            ad.permissionsClient.addAll(
                resolveFrontPermissions(
                    permissionsChain,
                    ad.resolveRelationsTo(principal)
                )
            )
        }
    }
}