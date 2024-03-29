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

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import settings.database.Settings

internal class SqlDelightGuildSettingsDatabase(
    private val database: Settings,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : GuildSettings {

    override fun getString(guildId: String, key: String): Flow<String?> = database
        .guildSettingsQueries
        .get(guildId, key)
        .asFlow()
        .mapToOneOrNull(dispatcher)

    override suspend fun setString(guildId: String, key: String, value: String) {
        withContext(Dispatchers.IO) {
            database.guildSettingsQueries.set(guildId, key, value)
        }
    }

    override fun getAll(key: String): Flow<List<String>> = database
        .guildSettingsQueries
        .getAll(key)
        .asFlow()
        .mapToList(dispatcher)

    override suspend fun delete(guildId: String, key: String) {
        withContext(Dispatchers.IO) {
            database.guildSettingsQueries.delete(guildId, key)
        }
    }

    override suspend fun deleteAllForGuild(guildId: String) {
        withContext(Dispatchers.IO) {
            database.guildSettingsQueries.deleteAllForGuild(guildId)
        }
    }
}
