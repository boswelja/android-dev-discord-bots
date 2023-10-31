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

import settings.GuildSettings
import kotlinx.coroutines.flow.first
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import settings.ChannelSettings

/**
 * An implementation of [AndroidStudioUpdateSettings] backed by [GuildSettings].
 */
class AndroidStudioUpdateSettingsDatabase(
    private val channelSettings: ChannelSettings,
) : AndroidStudioUpdateSettings {
    override suspend fun getLastCheckInstant(): Instant {
        return channelSettings.getString("0", LastCheckInstantKey).first()?.let(Instant::parse)
            ?: Clock.System.now()
    }

    override suspend fun setLastCheckInstant(lastChecked: Instant) {
        channelSettings.setString("0", LastCheckInstantKey, lastChecked.toString())
    }

    override suspend fun enableUpdatesForChannel(channelId: String) {
        channelSettings.setString(channelId, TargetChannelKey, "")
    }

    override suspend fun disableUpdatesForChannel(channelId: String) {
        channelSettings.delete(channelId, TargetChannelKey)
    }

    override suspend fun getAllTargetChannels(): List<String> = channelSettings.getAll(TargetChannelKey).first()

    companion object {
        private const val LastCheckInstantKey = "lastCheckTime"
        private const val TargetChannelKey = "target_channel"
    }
}
