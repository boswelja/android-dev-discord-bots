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

import features.updates.androidstudio.updatesource.rssfetcher.Author
import features.updates.androidstudio.updatesource.rssfetcher.Entry
import features.updates.androidstudio.updatesource.rssfetcher.Feed
import features.updates.androidstudio.updatesource.rssfetcher.Fetcher
import features.updates.androidstudio.updatesource.rssfetcher.Link
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import java.time.OffsetDateTime
import kotlin.test.BeforeTest
import kotlin.test.assertEquals
import kotlin.test.assertNull

class AndroidStudioBlogUpdateSourceTest {

    private lateinit var mockFetcher: Fetcher

    private lateinit var testSubject: AndroidStudioBlogUpdateSource

    @BeforeTest
    fun setUp() {
        mockFetcher = mockk()
        testSubject = AndroidStudioBlogUpdateSource(mockFetcher)
    }

    @Test
    fun test_extractVersionFromTitle() {
        assertEquals(
            "Giraffe Canary 9",
            testSubject.extractVersionFromTitle("Android Studio Giraffe Canary 9 now available"),
        )
        assertEquals(
            "Flamingo Beta 5",
            testSubject.extractVersionFromTitle("Android Studio Flamingo Beta 5 now available"),
        )
        assertEquals(
            "Flamingo Beta 3",
            testSubject.extractVersionFromTitle("Android Studio Flamingo Beta 3 is now available"),
        )
        assertEquals(
            "Electric Eel Patch 1",
            testSubject.extractVersionFromTitle("Android Studio Electric Eel | 2022.1.1 Patch 1 now available"),
        )
        assertEquals(
            "Electric Eel",
            testSubject.extractVersionFromTitle("Android Studio Electric Eel available in the Stable channel"),
        )
        assertEquals(
            "Hedgehog RC 2",
            testSubject.extractVersionFromTitle("Android Studio Hedgehog | 2023.1.1 RC 2 now available")
        )
    }

    @Test
    fun test_extractChannelFromTitle() {
        assertEquals(
            AndroidStudioUpdate.UpdateChannel.Stable,
            testSubject.extractChannelFromTitle("Android Studio Giraffe Patch 2 is now available")
        )
        assertEquals(
            AndroidStudioUpdate.UpdateChannel.Stable,
            testSubject.extractChannelFromTitle("Android Studio Giraffe now available in the stable channel")
        )
        assertEquals(
            AndroidStudioUpdate.UpdateChannel.ReleaseCandidate,
            testSubject.extractChannelFromTitle("Android Studio Hedgehog | 2023.1.1 RC 3 now available")
        )
        assertEquals(
            AndroidStudioUpdate.UpdateChannel.Beta,
            testSubject.extractChannelFromTitle("Android Studio Hedgehog Beta 5 now available")
        )
        assertEquals(
            AndroidStudioUpdate.UpdateChannel.Beta,
            testSubject.extractChannelFromTitle("Android Studio Giraffe Beta 5 now available")
        )
        assertEquals(
            AndroidStudioUpdate.UpdateChannel.Canary,
            testSubject.extractChannelFromTitle("Android Studio Hedgehog Canary 7 now available")
        )
        assertEquals(
            AndroidStudioUpdate.UpdateChannel.Canary,
            testSubject.extractChannelFromTitle("Android Studio Iguana | 2023.2.1 Canary 13 now available")
        )
    }

    @Test
    fun before_checkForUpdates_latest_releases_are_null() = runTest {
        assertNull(testSubject.latestCanaryUpdate.first())
        assertNull(testSubject.latestBetaUpdate.first())
        assertNull(testSubject.latestReleaseCandidateUpdate.first())
        assertNull(testSubject.latestStableUpdate.first())
    }

    @Test
    fun after_checkForUpdates_latest_releases_retrieved() = runTest {
        coEvery { mockFetcher.obtainFeed(UPDATE_FEED_URL) } returns generateMockFeed()
        testSubject.checkForUpdates()

        assertEquals(
            AndroidStudioUpdate.UpdateChannel.Canary,
            testSubject.latestCanaryUpdate.first()?.updateChannel
        )
        assertEquals(
            AndroidStudioUpdate.UpdateChannel.Beta,
            testSubject.latestBetaUpdate.first()?.updateChannel
        )
        assertEquals(
            AndroidStudioUpdate.UpdateChannel.ReleaseCandidate,
            testSubject.latestReleaseCandidateUpdate.first()?.updateChannel
        )
        assertEquals(
            AndroidStudioUpdate.UpdateChannel.Stable,
            testSubject.latestStableUpdate.first()?.updateChannel
        )
    }

    private fun generateMockFeed(): Feed {
        return Feed(
            id = "feed",
            title = "Android Studio Release Updates",
            subtitle = "Provides official announcements for new versions of Android Studio and other Android developer tools.",
            author = Author("Feed Author"),
            entries = listOf(
                Entry(
                    id = "id1",
                    title = "Android Studio Iguana | 2023.2.1 Canary 13 now available",
                    author = Author("Android Studio Releaser"),
                    content = "",
                    links = listOf(Link("https://google.com")),
                    publishedOn = OffsetDateTime.now()
                ),
                Entry(
                    id = "id2",
                    title = "Android Studio Hedgehog Beta 5 now available",
                    author = Author("Android Studio Releaser"),
                    content = "",
                    links = listOf(Link("https://google.com")),
                    publishedOn = OffsetDateTime.now()
                ),
                Entry(
                    id = "id3",
                    title = "Android Studio Hedgehog | 2023.1.1 RC 3 now available",
                    author = Author("Android Studio Releaser"),
                    content = "",
                    links = listOf(Link("https://google.com")),
                    publishedOn = OffsetDateTime.now()
                ),
                Entry(
                    id = "id4",
                    title = "Android Studio Giraffe | 2022.3.1 Patch 3 now available",
                    author = Author("Android Studio Releaser"),
                    content = "",
                    links = listOf(Link("https://google.com")),
                    publishedOn = OffsetDateTime.now()
                ),
            ),
            links = listOf(),
            lastUpdatedOn = OffsetDateTime.now()
        )
    }
}
