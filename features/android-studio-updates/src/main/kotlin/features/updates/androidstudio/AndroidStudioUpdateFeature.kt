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
import features.updates.androidstudio.updatesource.AndroidStudioUpdate
import features.updates.androidstudio.updatesource.AndroidStudioUpdateSource
import features.updates.androidstudio.updatesource.createUpdateSource
import guildsettings.GuildSettingsDatabase
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import logging.logError
import logging.logInfo
import scheduler.scheduleRepeating
import kotlin.time.Duration.Companion.hours

/**
 * A [Feature] that configured a bot for checking and posting about new Android Studio updates.
 */
class AndroidStudioUpdateFeature(
    private val discordBotScope: DiscordBotScope,
    guildSettings: GuildSettingsDatabase,
    private val settings: AndroidStudioUpdateSettings = AndroidStudioUpdateSettingsDatabase(guildSettings),
    private val updateSource: AndroidStudioUpdateSource = createUpdateSource(),
) : Feature {

    override suspend fun init() {
        registerCommands()

        // Start the update checker loop. Cleanup is handled automagically
        coroutineScope {
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
                            settings.setTargetChannelForGuild(sourceGuildId!!, targetChannelId)
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
                            settings.removeTargetChannelForGuild(sourceGuildId!!)
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

    private suspend fun postNewUpdatesIfAny() {
        val lastCheckInstant = settings.getLastCheckInstant()
        val newUpdatesResult = updateSource.getUpdatesAfter(lastCheckInstant)
        if (newUpdatesResult.isFailure) return // TODO Handle failures
        val newUpdates = newUpdatesResult.getOrThrow()
        logInfo {
            val lastCheckHumanReadable = lastCheckInstant.toLocalDateTime(TimeZone.UTC).toString()
            "${newUpdates.count()} new Android Studio updates found since $lastCheckHumanReadable"
        }
        if (newUpdates.isEmpty()) return
        settings.setLastCheckInstant(Clock.System.now())

        val allTargets = settings.getAllTargetChannels()
        allTargets.forEach { targetChannelId ->
            try {
                val channelType = discordBotScope.getChannel(targetChannelId).type
                newUpdates.forEach { newUpdate ->
                    postMessageToChannel(targetChannelId, channelType, newUpdate)
                }
            } catch (e: Exception) {
                logError(e) { "Failed to notify $targetChannelId of a new Android Studio release." }
            }
        }
    }

    private suspend fun postMessageToChannel(
        channelId: String,
        channelType: Channel.Type,
        update: AndroidStudioUpdate,
    ) {
        when (channelType) {
            Channel.Type.GUILD_TEXT,
            Channel.Type.DM,
            Channel.Type.GROUP_DM,
            Channel.Type.GUILD_ANNOUNCEMENT,
            ->
                discordBotScope.createEmbed(channelId) {
                    title = update.fullVersionName
                    description = update.summary
                    timestamp = update.timestamp
                    url = update.url
                }
            Channel.Type.GUILD_FORUM ->
                discordBotScope.createForumPost(channelId, update.fullVersionName) {
                    title = update.fullVersionName
                    description = update.summary
                    timestamp = update.timestamp
                    url = update.url
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
}
