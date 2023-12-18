package com.crowdproj.repo.cassandra

import com.crowdproj.comments.repo.tests.RepoCommentUpdateTest

class RepoCommentCassandraUpdateTest : RepoCommentUpdateTest() {
    override val repo = CasandraTestContainer.repository(initObjects, "ks_update")
}