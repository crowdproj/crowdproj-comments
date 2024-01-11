package com.crowdproj.comments.biz.validation

import com.crowdproj.comments.common.CommentContext
import com.crowdproj.comments.common.helpers.errorValidation
import com.crowdproj.comments.common.helpers.fail
import com.crowdproj.comments.common.models.CommentState
import com.crowdproj.comments.common.models.CommentUserId
import com.crowdproj.comments.common.permissions.CommentsPrincipalModel
import com.crowdproj.kotlin.cor.handlers.CorChainDsl
import com.crowdproj.kotlin.cor.handlers.chain
import com.crowdproj.kotlin.cor.handlers.worker

fun CorChainDsl<CommentContext>.validatePrincipal(title: String) = chain {
    this.title = title
    on { this.state == CommentState.RUNNING }
    validatePrincipalNotNull("Validate principal is not null")
    validatePrincipalUserID("Validate client ID")
    validatePrincipalGroups("Validate client groups")
}

fun CorChainDsl<CommentContext>.validatePrincipalUserID(title: String) = worker {
    this.title = title
    on { this.state == CommentState.RUNNING && this.principal.id == CommentUserId.NONE }
    handle {
        fail(
            errorValidation(
                field = "principal-id",
                violationCode = "empty",
                description = "field must be not empty",
            )
        )
    }
}

fun CorChainDsl<CommentContext>.validatePrincipalGroups(title: String) = worker {
    this.title = title
    on { this.state == CommentState.RUNNING && this.principal.groups.isEmpty() }
    handle {
        fail(
            errorValidation(
                field = "principal-groups",
                violationCode = "empty",
                description = "field must be not empty",
            )
        )
    }
}

fun CorChainDsl<CommentContext>.validatePrincipalNotNull(title: String) = worker {
    this.title = title
    on { this.principal == CommentsPrincipalModel.NONE }
    handle {
        fail(
            errorValidation(
                field = "principal",
                violationCode = "empty",
                description = "field must be not empty",
            )
        )
    }
}