package studio

import guildsettings.GuildSettingsRepository
import kotlinx.coroutines.flow.first
import fetcher.Entry
import fetcher.Fetcher
import fetcher.FetcherFactory
import java.time.OffsetDateTime

/**
 * This utility checker is used to retrieve Android Studio blog updates, compare that with local data and return new
 * posts. Local data is updated automatically. See [getNewPosts] for more details.
 * TODO Actually implement this
 */
class AndroidStudioUpdateChecker(
    private val settingsRepository: GuildSettingsRepository,
    private val source: Fetcher = FetcherFactory.create(),
) {

    suspend fun getNewPosts(): List<Entry> {
        val deserializedResponse = source.obtainFeed("https://androidstudio.googleblog.com/feeds/posts/default")
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
