package com.crowdproj.comments.repo.tests

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext

fun runRepoTest(testBody: suspend TestScope.() -> Unit) = runTest {
    withContext(Dispatchers.Default) {
        testBody()
    }
}