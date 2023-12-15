import com.crowdproj.comments.repo.inmemory.CommentsRepoInMemory
import com.crowdproj.comments.repo.tests.RepoCommentUpdateTest

class CommentRepoInMemoryUpdateTest: RepoCommentUpdateTest() {
    override val repo = CommentsRepoInMemory(
        initObjects = initObjects,
    )
}