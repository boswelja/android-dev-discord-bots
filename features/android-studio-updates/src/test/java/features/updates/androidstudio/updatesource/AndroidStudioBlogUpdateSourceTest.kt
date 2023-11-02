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

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class AndroidStudioBlogUpdateSourceTest {

    private val testSubject = AndroidStudioBlogUpdateSource()

    @Test
    fun text_extractVersionFromTitle() {
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
}
