package com.crowdproj.comments.app.repo

import com.crowdproj.comments.repo.inmemory.CommentsRepoInMemory

class CommentsInMemoryTest: CommentsRepoApiTest() {

    override val repo = CommentsRepoInMemory(initObjects = initComments)
}