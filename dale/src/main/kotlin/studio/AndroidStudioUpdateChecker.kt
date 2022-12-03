package studio

import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
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

    private val xmlMapper = XmlMapper().registerKotlinModule()

    suspend fun getNewPosts(): List<StudioBlogEntry> {
        val response = httpClient.get("https://androidstudio.googleblog.com/feeds/posts/default").bodyAsText()
        return xmlMapper.readValue(response, StudioBlogFeed::class.java).entry
    }
}