package studio

import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import java.io.FileReader
import java.io.FileWriter
import java.time.OffsetDateTime
import java.util.Properties

/**
 * This utility checker is used to retrieve Android Studio blog updates, compare that with local data and return new
 * posts. Local data is updated automatically. See [getNewPosts] for more details.
 * TODO Actually implement this
 */
class AndroidStudioUpdateChecker(
    private val httpClient: HttpClient = HttpClient(CIO)
) {

    private val xmlMapper = XmlMapper()
        .registerKotlinModule()
        .registerModule(JavaTimeModule())

    suspend fun getNewPosts(): List<StudioBlogEntry> {
        val response = httpClient.get("https://androidstudio.googleblog.com/feeds/posts/default").bodyAsText()
        val deserializedResponse = xmlMapper.readValue(response, StudioBlogFeed::class.java)
        val lastCheckTime = getLastCheckTime()
        val newEntries = if (lastCheckTime == null) {
            deserializedResponse.entry
        } else {
            deserializedResponse.entry.filter { it.publishedOn > lastCheckTime }
        }
        updateLastCheckTime(deserializedResponse.lastUpdatedOn)
        return newEntries
    }

    private fun updateLastCheckTime(newDate: OffsetDateTime) {
        val props = Properties()
        props["lastCheckTime"] = newDate.toString()
        props.store(FileWriter("dale.properties"), "")
    }

    private fun getLastCheckTime(): OffsetDateTime? {
        val props = Properties()
        props.load(FileReader("dale.properties"))
        return props["lastCheckTime"]?.let { OffsetDateTime.parse(it.toString()) }
    }
}