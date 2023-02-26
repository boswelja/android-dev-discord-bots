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
package discord.interaction

/**
 * A scope interface for managing application commands.
 */
interface ApplicationCommandScope {

    /**
     * Registers a new global chat input command. A "global chat input command" is a slash command that can be used
     * anywhere.
     * @param name The name of the command. This will be what users type to invoke the command.
     * @param description A short description of the command. This will be displayed to users next to the name.
     * @param onCommandInvoked Called when the command was invoked by the user. That is, when a new interaction is
     * initiated. See [InteractionScope].
     * @param builder The command builder. See [CommandBuilder].
     */
    suspend fun registerGlobalChatInputCommand(
        name: String,
        description: String,
        onCommandInvoked: suspend InteractionScope.() -> Unit,
        builder: CommandBuilder.() -> Unit,
    )

    /**
     * Registers a new global chat input command. A "global chat input command" is a slash command that can be used
     * anywhere.
     * @param name The name of the command. This will be what users type to invoke the command.
     * @param description A short description of the command. This will be displayed to users next to the name.
     * @param builder The command builder. See [CommandBuilder].
     */
    suspend fun registerGlobalChatInputCommandGroup(
        name: String,
        description: String,
        builder: CommandGroupBuilder.() -> Unit,
    )
}
