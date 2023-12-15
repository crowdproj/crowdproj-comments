import com.crowdproj.comments.repo.inmemory.CommentsRepoInMemory
import com.crowdproj.comments.repo.tests.RepoCommentSearchTest

class CommentRepoInMemorySearchTest: RepoCommentSearchTest() {
    override val repo = CommentsRepoInMemory(
        initObjects = initObjects,
    )
}