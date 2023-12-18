package com.crowdproj.repo.cassandra

import com.crowdproj.comments.repo.tests.*

class RepoCommentCassandraSearchTest : RepoCommentSearchTest() {
    override val repo = CasandraTestContainer.repository(initObjects, "ks_search")
}
