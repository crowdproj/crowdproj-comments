package com.crowdproj.repo.cassandra

import com.crowdproj.comments.repo.tests.RepoCommentDeleteTest

class RepoCommentCassandraDeleteTest : RepoCommentDeleteTest() {
    override val repo = TestCasandraContainer.repository(initObjects, "ks_delete")
}