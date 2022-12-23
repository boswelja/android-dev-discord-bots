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
package interaction

import DiscordBotScopeMarker

@ChatInputCommandBuilderMarker
interface ChatInputCommandBuilder : CommandBuilder {

    fun subCommand(name: String, description: String, builder: SubCommandBuilder.() -> Unit)

    fun subCommandGroup(name: String, description: String, builder: SubCommandGroupBuilder.() -> Unit)
}

@ChatInputCommandBuilderMarker
interface CommandBuilder {
    fun int(name: String, description: String, required: Boolean)

    fun string(name: String, description: String, required: Boolean)

    fun boolean(name: String, description: String, required: Boolean)

    fun user(name: String, description: String, required: Boolean)

    fun channel(name: String, description: String, required: Boolean)

    fun mentionable(name: String, description: String, required: Boolean)

    fun number(name: String, description: String, required: Boolean)
}

@ChatInputCommandBuilderMarker
interface SubCommandBuilder : CommandBuilder

@ChatInputCommandBuilderMarker
interface SubCommandGroupBuilder {
    fun subCommand(name: String, description: String, builder: SubCommandBuilder.() -> Unit)
}

@DslMarker
annotation class ChatInputCommandBuilderMarker
