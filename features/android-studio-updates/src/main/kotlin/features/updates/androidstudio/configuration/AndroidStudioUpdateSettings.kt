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
package features.updates.androidstudio.configuration

/**
 * Allows storing and retrieving settings specific to Android Studio update checking.
 */
interface AndroidStudioUpdateSettings {

    /**
     * Stores a new target channel ID to receive Android Studio updates for the guild with the given ID.
     */
    suspend fun enableUpdatesForChannel(channelId: String)

    /**
     * Removes any existing target channel for the guild with the given ID.
     */
    suspend fun disableUpdatesForChannel(channelId: String)

    /**
     * Get all target channels from all guilds.
     */
    suspend fun getAllTargetChannels(): List<String>
}
