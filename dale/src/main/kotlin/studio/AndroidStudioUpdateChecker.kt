package studio

import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import guildsettings.GuildSettingsRepository
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.flow.first
import rss.RssEntry
import rss.RssFeed
import java.time.OffsetDateTime

/**
 * This utility checker is used to retrieve Android Studio blog updates, compare that with local data and return new
 * posts. Local data is updated automatically. See [getNewPosts] for more details.
 * TODO Actually implement this
 */
class AndroidStudioUpdateChecker(
    private val settingsRepository: GuildSettingsRepository,
    private val httpClient: HttpClient = HttpClient(CIO),
) {

    private val xmlMapper = XmlMapper()
        .registerKotlinModule()
        .registerModule(JavaTimeModule())

    suspend fun getNewPosts(): List<RssEntry> {
        val response = httpClient.get("https://androidstudio.googleblog.com/feeds/posts/default").bodyAsText()
        val deserializedResponse = xmlMapper.readValue(response, RssFeed::class.java)
        val lastCheckTime = getLastCheckTime()
        val newEntries = if (lastCheckTime == null) {
            deserializedResponse.entry
        } else {
            deserializedResponse.entry.filter { it.publishedOn > lastCheckTime }
        }
        updateLastCheckTime(deserializedResponse.lastUpdatedOn)
        return newEntries
    }

    private suspend fun updateLastCheckTime(newDate: OffsetDateTime) {
        settingsRepository.setString("0", "lastCheckTime", newDate.toString())
    }

    private suspend fun getLastCheckTime(): OffsetDateTime? {
        return settingsRepository.getString("0", "lastCheckTime").first()?.let { OffsetDateTime.parse(it) }
    }
}
