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
package features.updates.androidstudio.updatesource

import features.updates.androidstudio.updatesource.rssfetcher.Fetcher
import features.updates.androidstudio.updatesource.rssfetcher.FetcherFactory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.merge
import kotlinx.datetime.toKotlinInstant

internal class AndroidStudioBlogUpdateSource(
    private val source: Fetcher = FetcherFactory.create(),
) : AndroidStudioUpdateSource {

    private val _latestStable = MutableStateFlow<AndroidStudioUpdate?>(null)
    private val _latestRc = MutableStateFlow<AndroidStudioUpdate?>(null)
    private val _latestBeta = MutableStateFlow<AndroidStudioUpdate?>(null)
    private val _latestCanary = MutableStateFlow<AndroidStudioUpdate?>(null)

    override val latestStableUpdate: StateFlow<AndroidStudioUpdate?> = _latestStable
    override val latestReleaseCandidateUpdate: StateFlow<AndroidStudioUpdate?> = _latestRc
    override val latestBetaUpdate: StateFlow<AndroidStudioUpdate?> = _latestBeta
    override val latestCanaryUpdate: StateFlow<AndroidStudioUpdate?> = _latestCanary
    override val latestUpdate: Flow<AndroidStudioUpdate> =
        merge(
            latestStableUpdate.filterNotNull(),
            latestReleaseCandidateUpdate.filterNotNull(),
            latestBetaUpdate.filterNotNull(),
            latestCanaryUpdate.filterNotNull(),
        )

    override suspend fun checkForUpdates() {
        val feed = source.obtainFeed("https://androidstudio.googleblog.com/feeds/posts/default")
        val versionsInFeed = feed.entries
            .map {
                AndroidStudioUpdate(
                    version = extractVersionFromTitle(it.title),
                    summary = it.title, // TODO Can we extract a better summary?
                    timestamp = it.publishedOn.toInstant().toKotlinInstant(),
                    url = it.links.last().url,
                    updateChannel = extractChannelFromTitle(it.title),
                )
            }
            .sortedByDescending { it.timestamp }

        val lastStableInFeed = versionsInFeed
            .first { it.updateChannel == AndroidStudioUpdate.UpdateChannel.Stable }
        val lastRcInFeed = versionsInFeed
            .first { it.updateChannel == AndroidStudioUpdate.UpdateChannel.ReleaseCandidate }
        val lastBetaInFeed = versionsInFeed
            .first { it.updateChannel == AndroidStudioUpdate.UpdateChannel.Beta }
        val lastCanaryInFeed = versionsInFeed
            .first { it.updateChannel == AndroidStudioUpdate.UpdateChannel.Canary }

        _latestStable.value = lastStableInFeed
        _latestRc.value = lastRcInFeed
        _latestBeta.value = lastBetaInFeed
        _latestCanary.value = lastCanaryInFeed
    }

    internal fun extractVersionFromTitle(title: String): String {
        return when {
            title.contains("is") -> title.split(" is ").first()
            title.contains("now") -> title.split(" now ").first()
            title.contains("available") -> title.split("available").first()
            else -> error("Unknown title format $title")
        }.trim()
            .removePrefix("Android Studio ")
            .replace(Regex(" \\| \\d\\d\\d\\d\\.\\d\\.\\d"), "")
    }

    internal fun extractChannelFromTitle(title: String): AndroidStudioUpdate.UpdateChannel {
        return when {
            title.contains("canary", ignoreCase = true) -> AndroidStudioUpdate.UpdateChannel.Canary
            title.contains("beta", ignoreCase = true) -> AndroidStudioUpdate.UpdateChannel.Beta
            title.contains(" rc ", ignoreCase = true) -> AndroidStudioUpdate.UpdateChannel.ReleaseCandidate
            else -> AndroidStudioUpdate.UpdateChannel.Stable
        }
    }
}
