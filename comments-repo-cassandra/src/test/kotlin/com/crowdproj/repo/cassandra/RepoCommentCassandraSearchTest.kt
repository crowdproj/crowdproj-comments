package com.crowdproj.repo.cassandra

import com.crowdproj.comments.repo.tests.*

class RepoCommentCassandraSearchTest : RepoCommentSearchTest() {
    override val repo = TestCasandraContainer.repository(initObjects, "ks_search")
}
