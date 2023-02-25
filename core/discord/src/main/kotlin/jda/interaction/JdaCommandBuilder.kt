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

import interaction.CommandBuilder
import interaction.CommandGroupBuilder
import interaction.InteractionScope
import interaction.SubCommandBuilder
import interaction.SubCommandGroupBuilder
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.CommandData
import net.dv8tion.jda.api.interactions.commands.build.Commands
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData
import net.dv8tion.jda.api.interactions.commands.build.SubcommandGroupData

internal class JdaCommandBuilder(name: String, description: String) : CommandBuilder {

    private var command = Commands.slash(name, description)

    override fun int(name: String, description: String, required: Boolean) {
        command = command.addOption(OptionType.INTEGER, name, description, required)
    }

    override fun string(name: String, description: String, required: Boolean) {
        command = command.addOption(OptionType.STRING, name, description, required)
    }

    override fun boolean(name: String, description: String, required: Boolean) {
        command = command.addOption(OptionType.BOOLEAN, name, description, required)
    }

    override fun user(name: String, description: String, required: Boolean) {
        command = command.addOption(OptionType.USER, name, description, required)
    }

    override fun channel(name: String, description: String, required: Boolean) {
        command = command.addOption(OptionType.CHANNEL, name, description, required)
    }

    override fun mentionable(name: String, description: String, required: Boolean) {
        command = command.addOption(OptionType.MENTIONABLE, name, description, required)
    }

    override fun number(name: String, description: String, required: Boolean) {
        command = command.addOption(OptionType.NUMBER, name, description, required)
    }

    fun build(): CommandData {
        return command
    }
}

class JdaCommandGroupBuilder(
    private val commandName: String,
    description: String,
    private val registerCommandInvokedListener: (String, suspend InteractionScope.() -> Unit) -> Unit,
) : CommandGroupBuilder {

    private var command = Commands.slash(commandName, description)

    override fun subCommand(
        name: String,
        description: String,
        onCommandInvoked: suspend InteractionScope.() -> Unit,
        builder: SubCommandBuilder.() -> Unit,
    ) {
        command = command.addSubcommands(
            JdaSubCommandBuilder(name, description).apply(builder).build()
        )
        registerCommandInvokedListener("$commandName $name", onCommandInvoked)
    }

    override fun subCommandGroup(name: String, description: String, builder: SubCommandGroupBuilder.() -> Unit) {
        command = command.addSubcommandGroups(
            JdaSubCommandGroupBuilder(
                name,
                description
            ) { subcommandName, onCommandInvoked ->
                registerCommandInvokedListener("$commandName $subcommandName", onCommandInvoked)
            }.apply(builder).build()
        )
    }

    fun build(): SlashCommandData {
        return command
    }
}

internal class JdaSubCommandBuilder(name: String, description: String) : SubCommandBuilder {

    private var command = SubcommandData(name, description)

    override fun int(name: String, description: String, required: Boolean) {
        command = command.addOption(OptionType.INTEGER, name, description, required)
    }

    override fun string(name: String, description: String, required: Boolean) {
        command = command.addOption(OptionType.STRING, name, description, required)
    }

    override fun boolean(name: String, description: String, required: Boolean) {
        command = command.addOption(OptionType.BOOLEAN, name, description, required)
    }

    override fun user(name: String, description: String, required: Boolean) {
        command = command.addOption(OptionType.USER, name, description, required)
    }

    override fun channel(name: String, description: String, required: Boolean) {
        command = command.addOption(OptionType.CHANNEL, name, description, required)
    }

    override fun mentionable(name: String, description: String, required: Boolean) {
        command = command.addOption(OptionType.MENTIONABLE, name, description, required)
    }

    override fun number(name: String, description: String, required: Boolean) {
        command = command.addOption(OptionType.NUMBER, name, description, required)
    }

    fun build(): SubcommandData {
        return command
    }
}

internal class JdaSubCommandGroupBuilder(
    private val commandName: String,
    description: String,
    private val registerCommandInvokedListener: (String, suspend InteractionScope.() -> Unit) -> Unit,
) : SubCommandGroupBuilder {

    private var command = SubcommandGroupData(commandName, description)

    override fun subCommand(
        name: String,
        description: String,
        onCommandInvoked: suspend InteractionScope.() -> Unit,
        builder: SubCommandBuilder.() -> Unit,
    ) {
        command = command.addSubcommands(
            JdaSubCommandBuilder(name, description).apply(builder).build()
        )
        registerCommandInvokedListener("$commandName $name", onCommandInvoked)
    }


    fun build(): SubcommandGroupData {
        return command
    }
}
