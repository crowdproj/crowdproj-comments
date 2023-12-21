package com.crowdproj.repo.cassandra

import com.crowdproj.comments.repo.tests.RepoCommentReadTest

class RepoCommentCassandraReadTest : RepoCommentReadTest() {
    override val repo = CasandraTestContainer.repository(initObjects, "ks_read")
}