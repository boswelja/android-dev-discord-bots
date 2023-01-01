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

import dev.kord.rest.builder.interaction.GlobalChatInputCreateBuilder
import dev.kord.rest.builder.interaction.GroupCommandBuilder
import dev.kord.rest.builder.interaction.boolean
import dev.kord.rest.builder.interaction.channel
import dev.kord.rest.builder.interaction.group
import dev.kord.rest.builder.interaction.int
import dev.kord.rest.builder.interaction.mentionable
import dev.kord.rest.builder.interaction.number
import dev.kord.rest.builder.interaction.string
import dev.kord.rest.builder.interaction.subCommand
import dev.kord.rest.builder.interaction.user
import interaction.CommandBuilder
import interaction.CommandGroupBuilder
import interaction.InteractionScope
import interaction.SubCommandBuilder
import interaction.SubCommandGroupBuilder

internal class KordCommandBuilder(
    private val kordBuilder: GlobalChatInputCreateBuilder,
) : CommandBuilder {

    override fun int(name: String, description: String, required: Boolean) =
        kordBuilder.int(name, description) {
            this.required = required
        }

    override fun string(name: String, description: String, required: Boolean) =
        kordBuilder.string(name, description) {
            this.required = required
        }

    override fun boolean(name: String, description: String, required: Boolean) =
        kordBuilder.boolean(name, description) {
            this.required = required
        }

    override fun user(name: String, description: String, required: Boolean) =
        kordBuilder.user(name, description) {
            this.required = required
        }

    override fun channel(name: String, description: String, required: Boolean) =
        kordBuilder.channel(name, description) {
            this.required = required
        }

    override fun mentionable(name: String, description: String, required: Boolean) =
        kordBuilder.mentionable(name, description) {
            this.required = required
        }

    override fun number(name: String, description: String, required: Boolean) =
        kordBuilder.number(name, description) {
            this.required = required
        }
}

internal class KordCommandGroupBuilder(
    private val kordBuilder: GlobalChatInputCreateBuilder,
) : CommandGroupBuilder {

    val commandInvokeCallbacks = mutableMapOf<String, suspend InteractionScope.() -> Unit>()

    override fun subCommand(
        name: String,
        description: String,
        onCommandInvoked: suspend InteractionScope.() -> Unit,
        builder: SubCommandBuilder.() -> Unit,
    ) {
        kordBuilder.subCommand(name, description) {
            KordSubCommandBuilder(this).apply(builder)
        }
        commandInvokeCallbacks[name] = onCommandInvoked
    }

    override fun subCommandGroup(name: String, description: String, builder: SubCommandGroupBuilder.() -> Unit) {
        kordBuilder.group(name, description) {
            KordSubCommandGroupBuilder(this).apply(builder).also {
                commandInvokeCallbacks.putAll(
                    it.commandInvokeCallbacks.mapKeys {
                        "$name ${it.key}"
                    },
                )
            }
        }
    }
}

internal class KordSubCommandBuilder(
    private val kordBuilder: dev.kord.rest.builder.interaction.SubCommandBuilder,
) : SubCommandBuilder {

    override fun int(name: String, description: String, required: Boolean) =
        kordBuilder.int(name, description) {
            this.required = required
        }

    override fun string(name: String, description: String, required: Boolean) =
        kordBuilder.string(name, description) {
            this.required = required
        }

    override fun boolean(name: String, description: String, required: Boolean) =
        kordBuilder.boolean(name, description) {
            this.required = required
        }

    override fun user(name: String, description: String, required: Boolean) =
        kordBuilder.user(name, description) {
            this.required = required
        }

    override fun channel(name: String, description: String, required: Boolean) =
        kordBuilder.channel(name, description) {
            this.required = required
        }

    override fun mentionable(name: String, description: String, required: Boolean) =
        kordBuilder.mentionable(name, description) {
            this.required = required
        }

    override fun number(name: String, description: String, required: Boolean) =
        kordBuilder.number(name, description) {
            this.required = required
        }
}

internal class KordSubCommandGroupBuilder(
    private val kordBuilder: GroupCommandBuilder,
) : SubCommandGroupBuilder {

    val commandInvokeCallbacks = mutableMapOf<String, suspend InteractionScope.() -> Unit>()

    override fun subCommand(
        name: String,
        description: String,
        onCommandInvoked: suspend InteractionScope.() -> Unit,
        builder: SubCommandBuilder.() -> Unit,
    ) {
        kordBuilder.subCommand(name, description) {
            KordSubCommandBuilder(this).apply(builder)
        }
        commandInvokeCallbacks[name] = onCommandInvoked
    }
}
