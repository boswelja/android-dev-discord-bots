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
package discord.jda.interaction

import discord.interaction.ApplicationCommandScope
import discord.interaction.CommandBuilder
import discord.interaction.CommandGroupBuilder
import discord.interaction.InteractionScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.events.GenericEvent
import net.dv8tion.jda.api.events.interaction.command.GenericCommandInteractionEvent
import net.dv8tion.jda.api.hooks.EventListener

internal class JdaApplicationCommandScope(
    private val coroutineScope: CoroutineScope,
    private val jda: JDA,
) : ApplicationCommandScope {
    override suspend fun registerGlobalChatInputCommand(
        name: String,
        description: String,
        onCommandInvoked: suspend InteractionScope.() -> Unit,
        builder: CommandBuilder.() -> Unit,
    ) {
        withContext(Dispatchers.IO) {
            jda.upsertCommand(JdaCommandBuilder(name, description).apply(builder).build()).complete()
            jda.addEventListener(CommandEventListener(coroutineScope, name, onCommandInvoked))
        }
    }

    override suspend fun registerGlobalChatInputCommandGroup(
        name: String,
        description: String,
        builder: CommandGroupBuilder.() -> Unit,
    ) {
        withContext(Dispatchers.IO) {
            val command = JdaCommandGroupBuilder(
                name,
                description,
                ::registerCommandInvokedListener,
            ).apply(builder).build()
            jda.upsertCommand(command).complete()
        }
    }

    private fun registerCommandInvokedListener(
        fullCommandName: String,
        onCommandInvoked: suspend InteractionScope.() -> Unit,
    ) {
        jda.addEventListener(CommandEventListener(coroutineScope, fullCommandName, onCommandInvoked))
    }
}

internal class CommandEventListener(
    private val coroutineScope: CoroutineScope,
    private val fullCommandName: String,
    private val onCommandInvoked: suspend InteractionScope.() -> Unit,
) : EventListener {
    override fun onEvent(event: GenericEvent) {
        if (event !is GenericCommandInteractionEvent) return

        if (event.fullCommandName == fullCommandName) {
            val interactionScope = JdaInteractionScope(event)

            coroutineScope.launch {
                onCommandInvoked(interactionScope)
            }
        }
    }
}
