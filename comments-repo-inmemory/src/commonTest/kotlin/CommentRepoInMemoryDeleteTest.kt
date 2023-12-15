import com.crowdproj.comments.repo.inmemory.CommentsRepoInMemory
import com.crowdproj.comments.repo.tests.RepoCommentDeleteTest

class CommentRepoInMemoryDeleteTest: RepoCommentDeleteTest() {
    override val repo = CommentsRepoInMemory(
        initObjects = initObjects,
    )
}