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

import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToOneOrNull
import guildsettings.database.GuildSettings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import org.sqlite.SQLiteException

internal class SqlDelightGuildSettingsDatabase(
    private val driver: SqlDriver,
) : GuildSettingsDatabase {
    private val database = GuildSettings(driver)

    init {
        try {
            GuildSettings.Schema.create(driver)
        } catch (_: SQLiteException) {
            // This probably means the table already exists, so ignore exceptions
        }
    }

    override fun getString(guildId: String, key: String): Flow<String?> {
        return database.guildSettingsQueries.get(guildId, key).asFlow().mapToOneOrNull()
    }

    override suspend fun setString(guildId: String, key: String, value: String) {
        withContext(Dispatchers.IO) {
            database.guildSettingsQueries.set(guildId, key, value)
        }
    }
}
