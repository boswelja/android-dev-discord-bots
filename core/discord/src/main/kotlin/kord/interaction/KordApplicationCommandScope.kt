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

import dev.kord.gateway.Gateway
import dev.kord.gateway.InteractionCreate
import dev.kord.rest.builder.interaction.GroupCommandBuilder
import dev.kord.rest.builder.interaction.boolean
import dev.kord.rest.builder.interaction.channel
import dev.kord.rest.builder.interaction.int
import dev.kord.rest.builder.interaction.mentionable
import dev.kord.rest.builder.interaction.number
import dev.kord.rest.builder.interaction.string
import dev.kord.rest.builder.interaction.user
import dev.kord.rest.service.RestClient
import interaction.ApplicationCommandScope
import interaction.ChatInputCommandBuilder
import interaction.InteractionScope
import interaction.SubCommandBuilder
import interaction.SubCommandGroupBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.launch

internal class KordApplicationCommandScope(
    private val restClient: RestClient,
    private val gateway: Gateway,
    private val scope: CoroutineScope
) : ApplicationCommandScope {
    override suspend fun registerGlobalChatInputCommand(
        name: String,
        description: String,
        onCommandInvoked: suspend InteractionScope.() -> Unit,
        builder: ChatInputCommandBuilder.() -> Unit,
    ) {
        restClient.interaction.createGlobalChatInputApplicationCommand(
            restClient.application.getCurrentApplicationInfo().id,
            name,
            description
        ) { KordChatInputCommandBuilder(this).apply(builder) }

        scope.launch {
            gateway.events
                .filterIsInstance<InteractionCreate>()
                .collectLatest {
                    println(it)
                    val interactionScope = KordInteractionScope(restClient, it)
                    interactionScope.onCommandInvoked()
                }
        }
    }
}
