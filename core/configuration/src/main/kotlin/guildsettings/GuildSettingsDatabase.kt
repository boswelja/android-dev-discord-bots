/*
 * Copyright 2022 AndroidDev Discord Dev Team
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
package guildsettings

import kotlinx.coroutines.flow.Flow

// TODO write higher level functions to alter the DB and expose those
interface GuildSettingsDatabase {
    fun getString(guildId: String, key: String): Flow<String?>

    fun getAll(key: String): Flow<List<String>>

    suspend fun setString(guildId: String, key: String, value: String)

    suspend fun delete(guildId: String, key: String)

    suspend fun deleteAllForGuild(guildId: String)
}
