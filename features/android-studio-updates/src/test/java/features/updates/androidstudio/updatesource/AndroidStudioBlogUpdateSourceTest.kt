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
import io.mockk.mockk
import org.junit.jupiter.api.Test
import kotlin.test.BeforeTest
import kotlin.test.assertEquals

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
}
