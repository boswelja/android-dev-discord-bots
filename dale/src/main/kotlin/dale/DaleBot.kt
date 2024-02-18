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
package dale

import dev.kord.common.entity.Permission
import dev.kord.common.entity.Permissions
import dev.kord.core.Kord
import dev.kord.core.behavior.interaction.respondEphemeral
import dev.kord.core.entity.interaction.GroupCommand
import dev.kord.core.event.interaction.ChatInputCommandInteractionCreateEvent
import dev.kord.core.on
import dev.kord.rest.builder.interaction.boolean
import dev.kord.rest.builder.interaction.channel
import dev.kord.rest.builder.interaction.string
import dev.kord.rest.builder.interaction.subCommand
import feature.ChatInteraction
import feature.ChatInteractionGroup
import feature.Feature
import feature.FeatureHost
import feature.Interaction
import feature.Parameter
import feature.ResponseScope
import feature.TextBasedInteraction
import features.updates.androidstudio.AndroidStudioUpdateFeature
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import settings.SettingsDatabaseFactory

/**
 * A lifecycle-aware application that initializes and manages the Dale bot.
 */
class DaleBot(
    apiKey: String
) : FeatureHost() {

    private val kord = runBlocking { Kord(apiKey) }
    private val channelSettings = SettingsDatabaseFactory.channelSettingsInstance("dale")

    override suspend fun getFeatures(): List<Feature> = listOf(
        AndroidStudioUpdateFeature(kord, channelSettings)
    )

    override suspend fun registerInteraction(interaction: Interaction) {
        when (interaction) {
            is TextBasedInteraction -> registerTextBasedInteraction(interaction)
        }
    }

    private suspend fun registerTextBasedInteraction(interaction: TextBasedInteraction) {
        when (interaction) {
            is ChatInteraction -> TODO()
            is ChatInteractionGroup -> {
                kord.createGlobalChatInputCommand(
                    name = interaction.name,
                    description = interaction.description
                ) {
                    defaultMemberPermissions = when (interaction.permissionLevel) {
                        Interaction.PermissionLevel.Everyone -> null
                        Interaction.PermissionLevel.Moderator -> Permissions(Permission.ManageGuild)
                    }
                    interaction.subcommands.forEach { subcommand ->
                        subCommand(
                            name = subcommand.name,
                            description = subcommand.description
                        ) {
                            subcommand.parameters.forEach { parameter ->
                                when (parameter.type) {
                                    Parameter.Type.String -> string(
                                        name = parameter.name,
                                        description = parameter.description,
                                    ) { required = parameter.required }
                                    Parameter.Type.Bool -> boolean(
                                        name = parameter.name,
                                        description = parameter.description,
                                    ) { required = parameter.required }
                                    Parameter.Type.Channel -> channel(
                                        name = parameter.name,
                                        description = parameter.description,
                                    ) { required = parameter.required }
                                }
                            }
                        }
                    }
                }
                kord.on<ChatInputCommandInteractionCreateEvent> {
                    val command = this.interaction.command
                    if (command is GroupCommand && command.rootName == interaction.name) {
                        val responseScope = object : ResponseScope {
                            override suspend fun acknowledge() {
                                this@on.interaction.deferEphemeralResponse()
                            }

                            override suspend fun respond(text: String) {
                                this@on.interaction.respondEphemeral { content = text }
                            }
                        }
                        val invokedCommand = interaction.subcommands.firstOrNull {
                            command.name == it.name
                        }
                        invokedCommand?.onInvoke?.invoke(responseScope, command.options.mapValues { it.value.value!! })
                    }
                }
            }
        }
    }

    override fun onFeaturesRegistered() {
        lifecycleScope.launch {
            kord.login()
        }
    }
}
