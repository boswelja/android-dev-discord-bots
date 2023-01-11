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

import dev.kord.common.entity.CommandArgument
import dev.kord.common.entity.CommandGroup
import dev.kord.common.entity.SubCommand
import dev.kord.gateway.InteractionCreate
import dev.kord.rest.service.RestClient
import interaction.InteractionScope

internal class KordInteractionScope(
    private val restClient: RestClient,
    private val interaction: InteractionCreate,
) : InteractionScope {

    override val sourceGuildId: String? = interaction.interaction.guildId.value?.toString()

    override val sourceChannelId: String = interaction.interaction.channelId.toString()

    override suspend fun createResponseMessage(targetChannelId: String, ephemeral: Boolean, content: String) {
        restClient.interaction.createInteractionResponse(
            interaction.interaction.id,
            interaction.interaction.token,
            ephemeral,
        ) {
            this.content = content
        }
    }

    override fun getChannelId(optionName: String): String {
        // Try to get the value from the first command segment
        interaction.interaction.data.options.value?.firstOrNull { it.name == optionName }?.also {
            if (it is CommandArgument.ChannelArgument) {
                return it.value.toString()
            }
        }

        // Get possible values
        val commandGroup = interaction.interaction.data.options.value?.filterIsInstance<CommandGroup>()?.firstOrNull()
        val subCommand: SubCommand = if (commandGroup != null) {
            // If we have a command group, we have a nested subcommand
            commandGroup.options.value?.first() as SubCommand
        } else {
            // Else we just have a subcommand
            interaction.interaction.data.options.value?.filterIsInstance<SubCommand>()?.firstOrNull() as SubCommand
        }

        return subCommand.options.value!!
            .filterIsInstance<CommandArgument.ChannelArgument>()
            .first { it.name == optionName }
            .value
            .toString()
    }
}
