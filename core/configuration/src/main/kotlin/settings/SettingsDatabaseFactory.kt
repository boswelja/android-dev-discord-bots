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
package settings

import org.sqlite.SQLiteException
import settings.database.Settings
import sqldelight.SQLDelightDrivers

/**
 * A factory for producing instances of various settings stores.
 */
object SettingsDatabaseFactory {
    private val instances: MutableMap<String, Settings> = mutableMapOf()

    /**
     * Get an instance of [GuildSettings] for a given name, usually the bot name for clarity. The returned
     * instance may be reused if an instance for the same name already exists.
     */
    fun guildSettingsInstance(name: String): GuildSettings {
        return instances[name]?.let { SqlDelightGuildSettingsDatabase(it) } ?: synchronized(this) {
            val driver = SQLDelightDrivers.instance(name)
            val settingsDb = instances[name]
                ?: Settings(driver)
                    .also { instances[name] = it }
            try {
                Settings.Schema.create(driver)
            } catch (_: SQLiteException) {
                // This probably means the table already exists, so ignore exceptions
            }
            SqlDelightGuildSettingsDatabase(settingsDb)
        }
    }

    /**
     * Get an instance of [ChannelSettings] for a given name, usually the bot name for clarity. The returned
     * instance may be reused if an instance for the same name already exists.
     */
    fun channelSettingsInstance(name: String): ChannelSettings {
        return instances[name]?.let { SqlDelightChannelSettingsDatabase(it) } ?: synchronized(this) {
            val driver = SQLDelightDrivers.instance(name)
            val settingsDb = instances[name]
                ?: Settings(driver)
                    .also { instances[name] = it }
            try {
                Settings.Schema.create(driver)
            } catch (_: SQLiteException) {
                // This probably means the table already exists, so ignore exceptions
            }
            SqlDelightChannelSettingsDatabase(settingsDb)
        }
    }

    /**
     * Get an instance of [BotSettings] for a given name, usually the bot name for clarity. The returned
     * instance may be reused if an instance for the same name already exists.
     */
    fun botSettingsInstance(name: String): BotSettings {
        return instances[name]?.let { SqlDelightBotSettingsDatabase(it) } ?: synchronized(this) {
            val driver = SQLDelightDrivers.instance(name)
            val settingsDb = instances[name]
                ?: Settings(driver)
                    .also { instances[name] = it }
            try {
                Settings.Schema.create(driver)
            } catch (_: SQLiteException) {
                // This probably means the table already exists, so ignore exceptions
            }
            SqlDelightBotSettingsDatabase(settingsDb)
        }
    }
}
