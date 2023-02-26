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
package interaction

@ChatInputCommandBuilderMarker
interface CommandGroupBuilder {
    /**
     * Configures a new subcommand under this command.
     * @param name The name of the subcommand. This will be what users type to invoke the command.
     * @param description A short description of the subcommand. This will be displayed to users next to the name.
     * @param onCommandInvoked Called when the command was invoked by the user. That is, when a new interaction is
     * initiated. See [InteractionScope].
     * @param builder The command builder. See [SubCommandBuilder].
     */
    fun subCommand(
        name: String,
        description: String,
        onCommandInvoked: suspend InteractionScope.() -> Unit,
        builder: SubCommandBuilder.() -> Unit,
    )

    /**
     * Configures a new subcommand group under this command.
     * @param name The name of the subcommand group. This will be what users type to invoke the command.
     * @param description A short description of the subcommand group. This will be displayed to users next to the name.
     * @param builder The command builder. See [SubCommandGroupBuilder].
     */
    fun subCommandGroup(name: String, description: String, builder: SubCommandGroupBuilder.() -> Unit)
}

/**
 * A builder for chat input commands where options are present. An option is a parameter that commands can optionally
 * request from users.
 */
@ChatInputCommandBuilderMarker
interface CommandBuilder {

    /**
     * Adds a new Int option to the command.
     * @param name The name of the option. This is what users will type to pass a value for the option.
     * @param description A short description of the option. This is displayed alongside the name.
     * @param required Whether this option is required. If true, the user must supply it to invoke the command
     */
    fun int(name: String, description: String, required: Boolean)

    /**
     * Adds a new String option to the command.
     * @param name The name of the option. This is what users will type to pass a value for the option.
     * @param description A short description of the option. This is displayed alongside the name.
     * @param required Whether this option is required. If true, the user must supply it to invoke the command
     */
    fun string(name: String, description: String, required: Boolean)

    /**
     * Adds a new Boolean option to the command.
     * @param name The name of the option. This is what users will type to pass a value for the option.
     * @param description A short description of the option. This is displayed alongside the name.
     * @param required Whether this option is required. If true, the user must supply it to invoke the command
     */
    fun boolean(name: String, description: String, required: Boolean)

    /**
     * Adds a new User option to the command. A User option requires a Discord user to be specified.
     * @param name The name of the option. This is what users will type to pass a value for the option.
     * @param description A short description of the option. This is displayed alongside the name.
     * @param required Whether this option is required. If true, the user must supply it to invoke the command
     */
    fun user(name: String, description: String, required: Boolean)

    /**
     * Adds a new Channel option to the command. A Channel option requires a text or voice channel to be specified.
     * @param name The name of the option. This is what users will type to pass a value for the option.
     * @param description A short description of the option. This is displayed alongside the name.
     * @param required Whether this option is required. If true, the user must supply it to invoke the command
     */
    fun channel(name: String, description: String, required: Boolean)

    /**
     * Adds a new Mentionable option to the command. A Mentionable options requires any mentionable type to be
     * specified. This could be a user, channel etc.
     * @param name The name of the option. This is what users will type to pass a value for the option.
     * @param description A short description of the option. This is displayed alongside the name.
     * @param required Whether this option is required. If true, the user must supply it to invoke the command
     */
    fun mentionable(name: String, description: String, required: Boolean)

    /**
     * Adds a new Number option to the command.
     * @param name The name of the option. This is what users will type to pass a value for the option.
     * @param description A short description of the option. This is displayed alongside the name.
     * @param required Whether this option is required. If true, the user must supply it to invoke the command
     */
    fun number(name: String, description: String, required: Boolean)
}

/**
 * A builder for chat input subcommands.
 */
@ChatInputCommandBuilderMarker
interface SubCommandBuilder : CommandBuilder

/**
 * A builder for groups of chat input subcommands.
 */
@ChatInputCommandBuilderMarker
interface SubCommandGroupBuilder {

    /**
     * Configures a new subcommand under this command.
     * @param name The name of the subcommand. This will be what users type to invoke the command.
     * @param description A short description of the subcommand. This will be displayed to users next to the name.
     * @param onCommandInvoked Called when the command was invoked by the user. That is, when a new interaction is
     * initiated. See [InteractionScope].
     * @param builder The command builder. See [SubCommandBuilder].
     */
    fun subCommand(
        name: String,
        description: String,
        onCommandInvoked: suspend InteractionScope.() -> Unit,
        builder: SubCommandBuilder.() -> Unit,
    )
}

@DslMarker
annotation class ChatInputCommandBuilderMarker
