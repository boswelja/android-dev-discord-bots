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

import kotlinx.datetime.Instant

/**
 * A source for Android Studio updates.
 */
interface AndroidStudioUpdateSource {

    /**
     * Get a List of Android Studio updates that were created after the given Instant.
     */
    suspend fun getUpdatesAfter(after: Instant): Result<List<AndroidStudioUpdate>>
}

/**
 * Creates a new [AndroidStudioUpdateSource] instance.
 */
fun createUpdateSource(): AndroidStudioUpdateSource = AndroidStudioBlogUpdateSource()