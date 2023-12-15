import com.crowdproj.comments.repo.inmemory.CommentsRepoInMemory
import com.crowdproj.comments.repo.tests.RepoCommentReadTest

class CommentRepoInMemoryReadTest: RepoCommentReadTest() {
    override val repo = CommentsRepoInMemory(
        initObjects = initObjects,
    )
}