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
package studio

import DiscordBotScope
import features.Feature
import guildsettings.GuildSettingsDatabase
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.datetime.toKotlinInstant
import schedule

class AndroidStudioUpdateFeature(
    private val discordBotScope: DiscordBotScope,
    private val settings: GuildSettingsDatabase,
    private val updateChecker: AndroidStudioUpdateChecker = AndroidStudioUpdateChecker(settings)
) : Feature {

    private var updateCheckerJob: Job? = null

    private suspend fun tryScheduleUpdateCheck() {
        updateCheckerJob?.cancelAndJoin()
        updateCheckerJob = coroutineScope {
            launch {
                schedule(Repeating.Daily()) {
                    postNewUpdatesIfAny()
                }
            }
        }
    }

    private suspend fun registerCommands() {
        discordBotScope.registerGlobalChatInputCommandGroup(
            name = "updates",
            description = "Configure various update messages",
        ) {
            subCommandGroup(
                name = "android-studio",
                description = "Configure update messages for Android Studio releases",
            ) {
                subCommand(
                    name = "enable",
                    description = "Enable update messages for Android Studio releases",
                    onCommandInvoked = {
                        val targetChannelId = getChannelId("target")
                        createResponseMessage(sourceChannelId, true, "Enabled Android Studio update messages for <#${targetChannelId}>")
                        enableStudioUpdateNotifications(sourceGuildId!!, targetChannelId)
                    },
                ) {
                    channel(
                        name = "target",
                        description = "The channel to post update messages to",
                        required = true,
                    )
                }
                subCommand(
                    name = "disable",
                    description = "Disable update messages for Android Studio releases",
                    onCommandInvoked = {
                        disableStudioUpdateMessages(sourceGuildId!!)
                        createResponseMessage(sourceChannelId, true, "Disabled Android Studio update messages for this server")
                    },
                ) {
                    // No options here
                }
            }
        }
    }

    private suspend fun enableStudioUpdateNotifications(guildId: String, targetChannelId: String) {
        settings.setString(guildId, TARGET_CHANNEL_KEY, targetChannelId)
        tryScheduleUpdateCheck()
    }

    private suspend fun disableStudioUpdateMessages(guildId: String) {
        settings.delete(guildId, TARGET_CHANNEL_KEY)
    }

    private suspend fun postNewUpdatesIfAny() {
        val newUpdates = updateChecker.getNewPosts()
        if (newUpdates.isNotEmpty()) {
            val allTargets = settings.getAll(TARGET_CHANNEL_KEY).first()
            newUpdates.forEach { newUpdate ->
                allTargets.forEach { targetChannelId ->
                    discordBotScope.createEmbed(targetChannelId) {
                        title = newUpdate.title
                        description = newUpdate.content
                        timestamp = newUpdate.publishedOn.toInstant().toKotlinInstant()
                        url = newUpdate.links.firstOrNull()?.url
                        author(newUpdate.author.name, null, null)
                    }
                }
            }
        }
    }

    override suspend fun init() {
        registerCommands()
        // If update check notifications are already enabled, try start the scheduler
        if (settings.getAll(TARGET_CHANNEL_KEY).first().isNotEmpty()) {
            tryScheduleUpdateCheck()
        }
    }

    companion object {
        private const val TARGET_CHANNEL_KEY = "target_channel"
    }
}
