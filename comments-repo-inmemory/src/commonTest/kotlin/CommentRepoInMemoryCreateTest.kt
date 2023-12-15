import com.crowdproj.comments.repo.inmemory.CommentsRepoInMemory
import com.crowdproj.comments.repo.tests.RepoCommentCreateTest

class CommentRepoInMemoryCreateTest: RepoCommentCreateTest() {
    override val repo = CommentsRepoInMemory(
        initObjects = initObjects,
    )
}