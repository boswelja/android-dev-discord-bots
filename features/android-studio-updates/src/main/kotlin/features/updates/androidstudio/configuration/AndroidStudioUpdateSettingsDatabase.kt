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

import guildsettings.GuildSettingsDatabase
import kotlinx.coroutines.flow.first
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

/**
 * An implementation of [AndroidStudioUpdateSettings] backed by [GuildSettingsDatabase].
 */
class AndroidStudioUpdateSettingsDatabase(
    private val guildSettingsDatabase: GuildSettingsDatabase,
) : AndroidStudioUpdateSettings {
    override suspend fun getLastCheckInstant(): Instant {
        return guildSettingsDatabase.getString("0", LastCheckInstantKey).first()?.let(Instant::parse)
            ?: Clock.System.now()
    }

    override suspend fun setLastCheckInstant(lastChecked: Instant) {
        guildSettingsDatabase.setString("0", LastCheckInstantKey, lastChecked.toString())
    }

    override suspend fun setTargetChannelForGuild(guildId: String, targetChannelId: String) {
        guildSettingsDatabase.setString(guildId, TargetChannelKey, targetChannelId)
    }

    override suspend fun removeTargetChannelForGuild(guildId: String) {
        guildSettingsDatabase.delete(guildId, TargetChannelKey)
    }

    override suspend fun getAllTargetChannels(): List<String> = guildSettingsDatabase.getAll(TargetChannelKey).first()

    companion object {
        private const val LastCheckInstantKey = "lastCheckTime"
        private const val TargetChannelKey = "target_channel"
    }
}