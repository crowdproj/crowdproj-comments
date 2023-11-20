import com.crowdproj.comments.api.v1.models.IRequest
import com.crowdproj.comments.api.v1.models.IResponse
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

val commentsApiV1Json = Json {
    ignoreUnknownKeys = true
}

fun IResponse.encode() = commentsApiV1Json.encodeToString(this)
fun IRequest.encode() = commentsApiV1Json.encodeToString(this)