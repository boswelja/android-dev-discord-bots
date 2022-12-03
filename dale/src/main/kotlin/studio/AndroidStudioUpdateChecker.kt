package studio

import guildsettings.GuildSettingsRepository
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.flow.first
import rss.RssEntry
import rss.RssParser
import rss.RssParserFactory
import java.time.OffsetDateTime

/**
 * This utility checker is used to retrieve Android Studio blog updates, compare that with local data and return new
 * posts. Local data is updated automatically. See [getNewPosts] for more details.
 * TODO Actually implement this
 */
class AndroidStudioUpdateChecker(
    private val settingsRepository: GuildSettingsRepository,
    private val parser: RssParser = RssParserFactory.create(),
    private val httpClient: HttpClient = HttpClient(CIO),
) {

    suspend fun getNewPosts(): List<RssEntry> {
        val response = httpClient.get("https://androidstudio.googleblog.com/feeds/posts/default").bodyAsText()
        val deserializedResponse = parser.parseFeed(response)
        val lastCheckTime = getLastCheckTime()
        val newEntries = if (lastCheckTime == null) {
            deserializedResponse.entries
        } else {
            deserializedResponse.entries.filter { it.publishedOn > lastCheckTime }
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
