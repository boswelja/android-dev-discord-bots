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

import dev.kord.common.entity.ChannelType
import dev.kord.common.entity.Permission
import dev.kord.common.entity.Permissions
import dev.kord.common.entity.Snowflake
import dev.kord.core.Kord
import dev.kord.core.behavior.channel.createEmbed
import dev.kord.core.behavior.interaction.response.respond
import dev.kord.core.entity.channel.Channel
import dev.kord.core.entity.channel.ForumChannel
import dev.kord.core.entity.channel.MessageChannel
import dev.kord.core.entity.interaction.GroupCommand
import dev.kord.core.event.interaction.ChatInputCommandInteractionCreateEvent
import dev.kord.core.on
import dev.kord.rest.builder.interaction.channel
import dev.kord.rest.builder.interaction.group
import dev.kord.rest.builder.message.create.embed
import features.Feature
import features.updates.androidstudio.configuration.AndroidStudioUpdateSettings
import features.updates.androidstudio.configuration.AndroidStudioUpdateSettingsDatabase
import features.updates.androidstudio.updatesource.AndroidStudioBlogUpdateSource
import features.updates.androidstudio.updatesource.AndroidStudioUpdate
import features.updates.androidstudio.updatesource.AndroidStudioUpdateSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.launch
import logging.logDebug
import logging.logError
import logging.logInfo
import scheduler.scheduleRepeating
import settings.ChannelSettings
import kotlin.time.Duration.Companion.hours

/**
 * A [Feature] that configured a bot for checking and posting about new Android Studio updates.
 */
class AndroidStudioUpdateFeature(
    private val kord: Kord,
    channelSettings: ChannelSettings,
    private val settings: AndroidStudioUpdateSettings = AndroidStudioUpdateSettingsDatabase(channelSettings),
    private val updateSource: AndroidStudioUpdateSource = AndroidStudioBlogUpdateSource(),
) : Feature {

    private val coroutineScope = CoroutineScope(SupervisorJob())

    override fun init() {
        coroutineScope.launch { registerCommands() }

        coroutineScope.launch {
            scheduleRepeating(interval = 1.hours) {
                logInfo { "Checking for new Android Studio updates" }
                updateSource.checkForUpdates()
            }
        }

        coroutineScope.launch {
            logInfo { "Starting update observer" }
            updateSource.latestUpdate
                .drop(SkipInitialUpdateCount)
                .collect {
                    logInfo { "Posting new update $it" }
                    postUpdate(it)
                }
            logError { "Stopped responding to new updates" }
        }
    }

    private suspend fun registerCommands() {
        kord.createGlobalChatInputCommand(
            name = "updates",
            description = "Configure various update messages",
        ) {
            defaultMemberPermissions = Permissions(Permission.ManageGuild)
            group(
                name = "android-studio",
                description = "Configure update messages for Android Studio releases",
            ) {
                subCommand(
                    name = "enable",
                    description = "Enable update messages for Android Studio releases",
                ) {
                    channel(
                        name = "target",
                        description = "The channel to post update messages to",
                    ) {
                        required = true
                        channelTypes = listOf(
                            ChannelType.GuildForum,
                            ChannelType.GuildNews,
                            ChannelType.GuildText,
                            ChannelType.PrivateThread,
                            ChannelType.PublicGuildThread,
                            ChannelType.PublicNewsThread,
                            ChannelType.DM
                        )
                    }
                }
                subCommand(
                    name = "disable",
                    description = "Disable update messages for Android Studio releases",
                ) {
                    channel(
                        name = "target",
                        description = "The channel to post update messages to",
                    ) {
                        required = true
                        channelTypes = listOf(
                            ChannelType.GuildForum,
                            ChannelType.GuildNews,
                            ChannelType.GuildText,
                            ChannelType.PrivateThread,
                            ChannelType.PublicGuildThread,
                            ChannelType.PublicNewsThread,
                            ChannelType.DM
                        )
                    }
                }
            }
        }
        kord.on<ChatInputCommandInteractionCreateEvent> {
            val command = interaction.command
            if (command is GroupCommand && command.rootName == "updates" && command.groupName == "android-studio") {
                val response = interaction.deferEphemeralResponse()
                val targetChannel = interaction.command.channels["target"]!!
                when (command.name) {
                    "enable" -> {
                        response.respond {
                            content = "Enabled Android Studio update messages for ${targetChannel.mention}"
                        }
                        settings.enableUpdatesForChannel(targetChannel.id.toString())
                        logDebug { "Enabled Android Studio updates for ${targetChannel.name}" }
                    }
                    "disable" -> {
                        settings.disableUpdatesForChannel(targetChannel.id.toString())
                        response.respond {
                            content = "Disabled Android Studio update messages for this server"
                        }
                        logDebug { "Disabled Android Studio updates for ${targetChannel.name}" }
                    }
                }
            }
        }
    }

    private suspend fun postUpdate(update: AndroidStudioUpdate) {
        val allTargets = settings.getAllTargetChannels()
        allTargets.forEach { targetChannelId ->
            try {
                val channel = kord.getChannel(Snowflake(targetChannelId))!!
                logDebug { "Attempting to notify ${channel.mention} of update" }
                postMessageToChannel(channel, update)
            } catch (e: Exception) {
                logError(e) { "Failed to notify $targetChannelId of a new Android Studio release." }
            }
        }
    }

    private suspend fun postMessageToChannel(
        channel: Channel,
        update: AndroidStudioUpdate,
    ) {
        when (channel) {
            is MessageChannel -> channel.createEmbed {
                title = "Android Studio ${update.version}"
                description = update.summary
                timestamp = update.timestamp
                url = update.url
            }
            is ForumChannel -> channel.startPublicThread(name = update.version) {
                message {
                    embed {
                        title = "Android Studio ${update.version}"
                        description = update.summary
                        timestamp = update.timestamp
                        url = update.url
                    }
                }
            }
            else -> error("Unsupported channel $channel")
        }
    }

    companion object {
        private val SkipInitialUpdateCount = AndroidStudioUpdate.UpdateChannel.entries.count()
    }
}
