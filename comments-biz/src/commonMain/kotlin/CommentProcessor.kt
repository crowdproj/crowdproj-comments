import com.crowdproj.comments.common.CommentContext
import com.crowdproj.comments.common.config.CommentsCorSettings
import com.crowdproj.comments.common.models.CommentCommand
import com.crowdproj.comments.common.models.CommentObjectType
import com.crowdproj.comments.common.models.CommentWorkMode
import com.crowdproj.comments.stubs.CommentStub

class CommentProcessor(private val settings: CommentsCorSettings = CommentsCorSettings()) {
    suspend fun exec(ctx: CommentContext){
        //TODO: Rewrite temporary stub solution with BIZ
        require(ctx.workMode == CommentWorkMode.STUB){
            "Currently working only in STUB mode."
        }
        when (ctx.command) {
            CommentCommand.SEARCH -> {
                ctx.commentsResponse.addAll(CommentStub.prepareSearchList("d-666-01", CommentObjectType.PRODUCT))
            }
            else -> {
                ctx.commentResponse = CommentStub.get()
            }
        }
    }
}