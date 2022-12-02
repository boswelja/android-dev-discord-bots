package studio

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*

/**
 * This utility checker is used to retrieve Android Studio blog updates, compare that with local data and return new
 * posts. Local data is updated automatically. See [getNewPosts] for more details.
 * TODO Actually implement this
 */
class AndroidStudioUpdateChecker(
    private val httpClient: HttpClient = HttpClient(CIO)
) {

    suspend fun getNewPosts(): List<Nothing> {
        val response = httpClient.get("https://androidstudio.googleblog.com/feeds/posts/default").bodyAsText()
        return emptyList() // TODO
    }
}