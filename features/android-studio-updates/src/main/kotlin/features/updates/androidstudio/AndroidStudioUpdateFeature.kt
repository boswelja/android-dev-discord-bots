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
package features.updates.androidstudio

import discord.DiscordBotScope
import discord.channel.Channel
import discord.guild.MemberPermission
import features.Feature
import features.updates.androidstudio.configuration.AndroidStudioUpdateSettings
import features.updates.androidstudio.configuration.AndroidStudioUpdateSettingsDatabase
import features.updates.androidstudio.updatesource.AndroidStudioUpdateSource
import features.updates.androidstudio.updatesource.createUpdateSource
import guildsettings.GuildSettingsDatabase
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import logging.logError
import logging.logInfo
import scheduler.scheduleRepeating
import kotlin.time.Duration.Companion.hours

/**
 * A [Feature] that configured a bot for checking and posting about new Android Studio updates.
 */
class AndroidStudioUpdateFeature(
    private val discordBotScope: DiscordBotScope,
    private val guildSettings: GuildSettingsDatabase,
    private val settings: AndroidStudioUpdateSettings = AndroidStudioUpdateSettingsDatabase(guildSettings),
    private val updateSource: AndroidStudioUpdateSource = createUpdateSource()
) : Feature {

    private var updateCheckerJob: Job? = null

    override suspend fun init() {
        registerCommands()

        // Start the update checker loop
        updateCheckerJob = coroutineScope {
            launch {
                scheduleRepeating(interval = 1.hours) {
                    logInfo { "Checking for new Android Studio updates" }
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
                        // If the guild member has permission, or is not a guild member (i.e. the command was triggered
                        // from DMs)
                        if (sourceGuildMember?.permissions?.contains(MemberPermission.MANAGE_SERVER) == true ||
                            sourceGuildMember == null
                        ) {
                            val targetChannelId = getChannelId("target")
                            createResponseMessage(
                                true,
                                "Enabled Android Studio update messages for <#$targetChannelId>",
                            )
                            enableStudioUpdateNotifications(sourceGuildId!!, targetChannelId)
                        } else {
                            // Else the user is a guild member and does not have permission
                            createResponseMessage(true, "You do not have permission to do that here")
                        }
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
                        // If the guild member has permission, or is not a guild member (i.e. the command was triggered
                        // from DMs)
                        if (sourceGuildMember?.permissions?.contains(MemberPermission.MANAGE_SERVER) == true ||
                            sourceGuildMember == null
                        ) {
                            disableStudioUpdateMessages(sourceGuildId!!)
                            createResponseMessage(true, "Disabled Android Studio update messages for this server")
                        } else {
                            // Else the user is a guild member and does not have permission
                            createResponseMessage(true, "You do not have permission to do that here")
                        }
                    },
                ) {
                    // No options here
                }
            }
        }
    }

    private suspend fun enableStudioUpdateNotifications(guildId: String, targetChannelId: String) {
        guildSettings.setString(guildId, TARGET_CHANNEL_KEY, targetChannelId)
    }

    private suspend fun disableStudioUpdateMessages(guildId: String) {
        guildSettings.delete(guildId, TARGET_CHANNEL_KEY)
    }

    @Suppress("NestedBlockDepth")
    private suspend fun postNewUpdatesIfAny() {
        val lastCheckInstant = settings.getLastCheckInstant()
        val newUpdatesResult = updateSource.getUpdatesAfter(lastCheckInstant)
        if (newUpdatesResult.isFailure) return // TODO Handle failures
        val newUpdates = newUpdatesResult.getOrThrow()
        logInfo { "${newUpdates.count()} new Android Studio updates found" }
        if (newUpdates.isEmpty()) return

        val allTargets = guildSettings.getAll(TARGET_CHANNEL_KEY).first()
        allTargets.forEach { targetChannelId ->
            try {
                val channelType = discordBotScope.getChannel(targetChannelId).type
                newUpdates.forEach { newUpdate ->
                    when (channelType) {
                        Channel.Type.GUILD_TEXT,
                        Channel.Type.DM,
                        Channel.Type.GROUP_DM,
                        Channel.Type.GUILD_ANNOUNCEMENT,
                        ->
                            discordBotScope.createEmbed(targetChannelId) {
                                title = newUpdate.fullVersionName
                                description = newUpdate.summary
                                timestamp = newUpdate.timestamp
                                url = newUpdate.url
                            }
                        Channel.Type.GUILD_FORUM ->
                            discordBotScope.createForumPost(targetChannelId, newUpdate.fullVersionName) {
                                title = newUpdate.fullVersionName
                                description = newUpdate.summary
                                timestamp = newUpdate.timestamp
                                url = newUpdate.url
                            }
                        Channel.Type.ANNOUNCEMENT_THREAD,
                        Channel.Type.PUBLIC_THREAD,
                        Channel.Type.PRIVATE_THREAD,
                        -> error("Threads are unsupported (for now)")
                        Channel.Type.GUILD_VOICE,
                        Channel.Type.GUILD_CATEGORY,
                        Channel.Type.GUILD_STAGE_VOICE,
                        -> error("Unsupported channel type $channelType")
                    }
                }
            } catch (e: Exception) {
                logError(e) { "Failed to notify $targetChannelId of a new Android Studio release." }
            }
        }
    }

    companion object {
        private const val TARGET_CHANNEL_KEY = "target_channel"
    }
}
