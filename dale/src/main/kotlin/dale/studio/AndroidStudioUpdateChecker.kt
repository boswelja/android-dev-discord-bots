/*
 * Copyright 2023 AndroidDev Discord Dev Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dale.studio

import com.apptasticsoftware.rssreader.Item
import com.apptasticsoftware.rssreader.RssReader
import guildsettings.GuildSettingsDatabase
import kotlinx.coroutines.flow.first
import java.time.OffsetDateTime
import java.util.stream.Collectors

/**
 * This utility checker is used to retrieve Android Studio blog updates, compare that with local data and return new
 * posts. Local data is updated automatically. See [getNewPosts] for more details.
 */
class AndroidStudioUpdateChecker(
    private val settingsRepository: GuildSettingsDatabase,
    private val source: RssReader = RssReader(),
) {

    /**
     * Get a List of feed entries that were created after the last check time.
     */
    suspend fun getNewPosts(): List<Item> {
        val lastCheckTime = getLastCheckTime()

        // If we haven't done an update check before, then return nothing. Otherwise, return posts created after the
        // last check time
        val newPosts: List<Item> = if (lastCheckTime == null) {
            emptyList()
        } else {
            source.read("https://androidstudio.googleblog.com/feeds/posts/default")
                .filter { OffsetDateTime.parse(it.pubDate.get()) > lastCheckTime }
                .collect(Collectors.toList())
        }

        updateLastCheckTime(OffsetDateTime.now())
        return newPosts
    }

    private suspend fun updateLastCheckTime(newDate: OffsetDateTime) =
        settingsRepository.setString("0", "lastCheckTime", newDate.toString())

    private suspend fun getLastCheckTime(): OffsetDateTime? =
        settingsRepository.getString("0", "lastCheckTime").first()?.let { OffsetDateTime.parse(it) }
}
