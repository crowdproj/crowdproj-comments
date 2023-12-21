package com.crowdproj.repo.cassandra

import com.crowdproj.comments.repo.tests.RepoCommentDeleteTest

class RepoCommentCassandraDeleteTest : RepoCommentDeleteTest() {
    override val repo = CasandraTestContainer.repository(initObjects, "ks_delete")
}