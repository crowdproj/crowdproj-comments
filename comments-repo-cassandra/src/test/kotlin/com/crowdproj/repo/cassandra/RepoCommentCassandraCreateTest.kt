package com.crowdproj.repo.cassandra

import com.crowdproj.comments.repo.tests.RepoCommentCreateTest

class RepoCommentCassandraCreateTest : RepoCommentCreateTest() {
    override val repo = CasandraTestContainer.repository(initObjects, "ks_create")
}