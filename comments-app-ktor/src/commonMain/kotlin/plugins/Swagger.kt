package com.crowdproj.comments.app.plugins

import com.crowdproj.comments.app.configs.CommentsAppSettings
import com.crowdproj.comments.app.resources.RESOURCES
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.utils.io.charsets.*
import kotlin.text.*
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

private const val SWAGGER_VERSION = "5.9.0"
private const val SWAGGER_URL = "https://unpkg.com/swagger-ui-dist@$SWAGGER_VERSION"
private val SWAGGER_HTML = """
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <meta
      name="description"
      content="SwaggerUI"
    />
    <title>CrowdProj Comments API</title>
    <link rel="stylesheet" href="${SWAGGER_URL}/swagger-ui.css" />
  </head>
  <body>
  <div id="swagger-ui"></div>
  <script src="${SWAGGER_URL}/swagger-ui-bundle.js" crossorigin></script>
  <script src="${SWAGGER_URL}/swagger-ui-standalone-preset.js" crossorigin></script>
  <script>
    window.onload = () => {
      window.ui = SwaggerUIBundle({
        urls: [
          {url: "./specs/spec-comments-v1.yaml", name: "Comments API V1"},
          // {url: "./specs-ad-v2.yaml", name: "Marketplace API V2"}
        ],
        dom_id: '#swagger-ui',
        deepLinking: true,
        presets: [
          SwaggerUIBundle.presets.apis,
          SwaggerUIStandalonePreset
        ],
        plugins: [
          SwaggerUIBundle.plugins.DownloadUrl
        ],
        layout: "StandaloneLayout"
      });
    };
  </script>
  </body>
</html>
""".trimIndent()

@ExperimentalEncodingApi
fun Routing.swagger(appConfig: CommentsAppSettings) {
    get("/") {
        call.respondText(
            status = HttpStatusCode.OK,
            text = SWAGGER_HTML,
            contentType = ContentType.Text.Html.withParameter(
                name = "charset",
                value = Charsets.UTF_8.name
            ),
        )
    }
    get(Regex("^specs/.*\\.yaml\$")) {
        val res = RESOURCES[call.request.path().removePrefix("/")]
        if (res != null) {
            call.respondText(
                status = HttpStatusCode.OK,
                text = Base64.decode(res).decodeToString().replaceServers(appConfig.appUrls),
                contentType = ContentType("application", "yaml").withParameter(
                    name = "charset",
                    value = Charsets.UTF_8.name
                ),
            )
        } else {
            call.respondText(
                status = HttpStatusCode.NotFound,
                text = "Not Found",
            )
        }
    }

}

fun String.replaceServers(urls: List<String>): String = replace(
    Regex(
        "(?<=^servers:\$\\n)[\\s\\S]*?(?=\\n\\w+:\$)",
        setOf(RegexOption.MULTILINE, RegexOption.IGNORE_CASE)
    ),
    urls.joinToString(separator = "\n") { "  - url: $it" }
)

