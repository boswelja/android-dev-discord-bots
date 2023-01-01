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
package kord.interaction

import dev.kord.common.entity.CommandGroup
import dev.kord.common.entity.SubCommand
import dev.kord.gateway.Gateway
import dev.kord.gateway.InteractionCreate
import dev.kord.rest.service.RestClient
import interaction.ApplicationCommandScope
import interaction.CommandBuilder
import interaction.CommandGroupBuilder
import interaction.InteractionScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.launch

internal class KordApplicationCommandScope(
    private val restClient: RestClient,
    private val gateway: Gateway,
    private val scope: CoroutineScope,
) : ApplicationCommandScope {
    override suspend fun registerGlobalChatInputCommand(
        name: String,
        description: String,
        onCommandInvoked: suspend InteractionScope.() -> Unit,
        builder: CommandBuilder.() -> Unit,
    ) {
        restClient.interaction.createGlobalChatInputApplicationCommand(
            restClient.application.getCurrentApplicationInfo().id,
            name,
            description,
        ) { KordCommandBuilder(this).apply(builder) }

        scope.launch {
            gateway.events
                .filterIsInstance<InteractionCreate>()
                .collectLatest {
                    if (it.interaction.data.name.value == name) {
                        val interactionScope = KordInteractionScope(restClient, it)
                        interactionScope.onCommandInvoked()
                    }
                }
        }
    }

    override suspend fun registerGlobalChatInputCommandGroup(
        name: String,
        description: String,
        builder: CommandGroupBuilder.() -> Unit,
    ) {
        var commandInvokeCallbacks: Map<String, suspend InteractionScope.() -> Unit>
        restClient.interaction.createGlobalChatInputApplicationCommand(
            restClient.application.getCurrentApplicationInfo().id,
            name,
            description,
        ) {
            KordCommandGroupBuilder(this).apply(builder).also {
                commandInvokeCallbacks = it.commandInvokeCallbacks
            }
        }

        scope.launch {
            gateway.events
                .filterIsInstance<InteractionCreate>()
                .collectLatest {
                    if (it.interaction.data.name.value == name) {
                        val commandGroup = it.interaction.data.options.value?.filterIsInstance<CommandGroup>()?.firstOrNull()
                        var fullCommandName = ""
                        val command = if (commandGroup != null) {
                            fullCommandName += commandGroup.name
                            commandGroup.options.value?.first() as SubCommand
                        } else {
                            it.interaction.data.options.value?.first() as SubCommand
                        }
                        fullCommandName += " ${command.name}"
                        fullCommandName = fullCommandName.trim()

                        println(fullCommandName)
                        println(commandInvokeCallbacks.keys)
                        val interactionScope = KordInteractionScope(restClient, it)
                        commandInvokeCallbacks.getValue(fullCommandName).invoke(interactionScope)
                    }
                }
        }
    }
}
