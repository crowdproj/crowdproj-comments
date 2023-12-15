package com.crowdproj.repo.cassandra

import com.crowdproj.comments.repo.tests.RepoCommentUpdateTest

class RepoCommentCassandraUpdateTest : RepoCommentUpdateTest() {
    override val repo = TestCasandraContainer.repository(initObjects, "ks_update")
}