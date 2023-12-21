package com.crowdproj.comments.app.repo

import com.crowdproj.comments.common.repo.ICommentsRepository

class CommentsCassandraApiTest: CommentsRepoApiTest() {

    override val repo: ICommentsRepository = CasandraTestContainer.repository(initObjects = initComments, "ks_api_test")
}