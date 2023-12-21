package com.crowdproj.comments.common.exceptions

import com.crowdproj.comments.common.models.CommentLock

class RepoConcurrencyException(expectedLock: CommentLock, actualLock: CommentLock?): RuntimeException(
    "Expected lock: $expectedLock, while actual lock: $actualLock"
)