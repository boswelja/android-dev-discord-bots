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
 * Describes an update for Android Studio.
 *
 * @property fullVersionName The full name of the version, ex. "Android Studio Giraffe Canary 9"
 * @property summary A short summary of this update. Usually 1-2 sentences.
 * @property timestamp The instant this update was made available.
 * @property url An HTTPS URL that links to a relevant page, ex. an article, or release notes.
 */
data class AndroidStudioUpdate(
    val fullVersionName: String,
    val summary: String,
    val timestamp: Instant,
    val url: String,
)
