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
package jda.interaction

import interaction.ApplicationCommandScope
import interaction.CommandBuilder
import interaction.CommandGroupBuilder
import interaction.InteractionScope
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
    private val jda: JDA
) : ApplicationCommandScope {
    private val commandInvocationMap = mutableMapOf<String, suspend InteractionScope.() -> Unit>()

    init {
        jda.registeredListeners.add(CommandEventListener())
    }

    override suspend fun registerGlobalChatInputCommand(
        name: String,
        description: String,
        onCommandInvoked: suspend InteractionScope.() -> Unit,
        builder: CommandBuilder.() -> Unit,
    ) {
        withContext(Dispatchers.IO) {
            jda.upsertCommand(name, description).complete()
            commandInvocationMap[name] = onCommandInvoked
        }
    }

    override suspend fun registerGlobalChatInputCommandGroup(
        name: String,
        description: String,
        builder: CommandGroupBuilder.() -> Unit,
    ) {
        withContext(Dispatchers.IO) {
            jda.upsertCommand(JdaCommandGroupBuilder(name, description).apply(builder).build()).complete()
        }
    }

    inner class CommandEventListener : EventListener {
        override fun onEvent(event: GenericEvent) {
            if (event !is GenericCommandInteractionEvent) return

            val interactionScope = JdaInteractionScope(event)

            coroutineScope.launch {
                commandInvocationMap[event.fullCommandName]?.invoke(interactionScope)
            }
        }
    }

}
