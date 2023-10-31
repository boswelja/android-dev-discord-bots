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
import dev.kord.common.entity.Snowflake
import dev.kord.common.entity.optional.Optional
import dev.kord.core.Kord
import dev.kord.core.behavior.channel.createEmbed
import dev.kord.core.behavior.interaction.response.respond
import dev.kord.core.entity.channel.Channel
import dev.kord.core.entity.channel.ForumChannel
import dev.kord.core.entity.channel.GuildChannel
import dev.kord.core.entity.channel.GuildMessageChannel
import dev.kord.core.entity.channel.MessageChannel
import dev.kord.core.event.interaction.GuildChatInputCommandInteractionCreateEvent
import dev.kord.core.on
import dev.kord.rest.builder.interaction.channel
import dev.kord.rest.builder.interaction.group
import dev.kord.rest.builder.message.create.embed
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
import kotlin.reflect.KProperty
import kotlin.time.Duration.Companion.hours

/**
 * A [Feature] that configured a bot for checking and posting about new Android Studio updates.
 */
class AndroidStudioUpdateFeature(
    private val discordBotScope: Kord,
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
        discordBotScope.createGlobalChatInputCommand(
            name = "updates",
            description = "Configure various update messages",
        ) {
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
                    }
                }
                subCommand(
                    name = "disable",
                    description = "Disable update messages for Android Studio releases",
                ) {}
            }
        }

        discordBotScope.on<GuildChatInputCommandInteractionCreateEvent> {
            val commandName = interaction.command.data.name.value ?: return@on
            val sourceGuildMember = interaction.user
            if (commandName.startsWith("updates android-studio")) {
                val response = interaction.deferEphemeralResponse()
                when {
                    commandName.endsWith("enable") -> {
                        // If the guild member has permission
                        if (sourceGuildMember.getPermissions().contains(Permission.ManageGuild)) {
                            val targetChannel = interaction.command.channels["target"]!!
                            response.respond {
                                content = "Enabled Android Studio update messages for ${targetChannel.mention}"
                            }
                            settings.setTargetChannelForGuild(interaction.guildId.toString(), targetChannel.id.toString())
                        } else {
                            // Else the user is a guild member and does not have permission
                            response.respond {
                                content = "You do not have permission to do that here"
                            }
                        }
                    }
                    commandName.endsWith("disable") -> {
                        // If the guild member has permission, or is not a guild member (i.e. the command was triggered
                        // from DMs)
                        if (sourceGuildMember.getPermissions().contains(Permission.ManageGuild)) {
                            settings.removeTargetChannelForGuild(interaction.guildId.toString())
                            response.respond {
                                content = "Disabled Android Studio update messages for this server"
                            }
                        } else {
                            // Else the user is a guild member and does not have permission
                            response.respond {
                                content = "You do not have permission to do that here"
                            }
                        }
                    }
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
                val channel = discordBotScope.getChannel(Snowflake(targetChannelId))!!
                newUpdates.forEach { newUpdate ->
                    postMessageToChannel(channel, newUpdate)
                }
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
            is MessageChannel -> {
                channel.createEmbed {
                    title = update.fullVersionName
                    description = update.summary
                    timestamp = update.timestamp
                    url = update.url
                }
            }
            is ForumChannel -> {
                channel.startPublicThread(name = update.fullVersionName) {
                    message {
                        embed {
                            title = update.fullVersionName
                            description = update.summary
                            timestamp = update.timestamp
                            url = update.url
                        }
                    }
                }
            }
            else -> error("Unsupported channel $channel")
        }
    }
}

private operator fun <T> Optional<T>.getValue(t: T?, property: KProperty<*>): T? {
    return value
}
