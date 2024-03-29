/*
 * Copyright 2024 AndroidDev Discord Dev Team
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
package settings

import kotlinx.coroutines.flow.Flow

/**
 * A generic data store designed to hold Channel-specific settings.
 */
interface ChannelSettings {

    /**
     * Flows the value of the entry matching the given key & channel ID.
     */
    fun getString(channelId: String, key: String): Flow<String?>

    /**
     * Flows a list of all values matching the given key across all channels.
     */
    fun getAll(key: String): Flow<List<String>>

    /**
     * Flows a list of all channel IDs where a key is set to a specific value.
     */
    fun getChannelsWhere(key: String, value: String): Flow<List<String>>

    /**
     * Sets a value to a key & channel ID combination.
     */
    suspend fun setString(channelId: String, key: String, value: String)

    /**
     * Deletes an entry matching the given key & channel ID.
     */
    suspend fun delete(channelId: String, key: String)

    /**
     * Deletes all entries matching the given channel ID.
     */
    suspend fun deleteAllForChannel(channelId: String)
}
