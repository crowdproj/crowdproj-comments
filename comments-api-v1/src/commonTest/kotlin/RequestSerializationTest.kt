import com.crowdproj.product.comments.api.v1.models.*
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals


class RequestSerializationTest {
    private val request: ICommentRequest = CommentCreateRequest(
        debug = CpBaseDebug(
            mode = CpRequestDebugMode.STUB,
            stub = CpRequestDebugStubs.SUCCESS
        ),
        comment = CommentCreateObject(
            objectType = "product",
            objectId = "122111",
            content = "This is comment",
            contentType = "plainText",
            userId = "22411"
        )
    )

    @Test
    fun serialize() {
        val json = Json.encodeToString(ICommentRequest.serializer(), request)

        assertContains(json, Regex("\"mode\":\\s*\"stub\""))
        assertContains(json, Regex("\"stub\":\\s*\"success\""))
        assertContains(json, Regex("\"objectType\":\\s*\"product\""))
        assertContains(json, Regex("\"objectId\":\\s*\"122111\""))
        assertContains(json, Regex("\"content\":\\s*\"This is comment\""))
        assertContains(json, Regex("\"contentType\":\\s*\"plainText\""))
        assertContains(json, Regex("\"userId\":\\s*\"22411\""))
    }

    @Test
    fun deserialize() {
        val json = Json.encodeToString(ICommentRequest.serializer(), request)
        val obj = Json.decodeFromString(ICommentRequest.serializer(), json) as CommentCreateRequest

        assertEquals(request, obj)
    }

}