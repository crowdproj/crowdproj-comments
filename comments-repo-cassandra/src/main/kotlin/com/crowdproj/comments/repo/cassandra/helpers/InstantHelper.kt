package com.crowdproj.comments.repo.cassandra.helpers

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

fun Clock.System.nowMillis() = Instant.fromEpochMilliseconds(now().toEpochMilliseconds())
