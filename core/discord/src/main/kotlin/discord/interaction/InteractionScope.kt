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

import discord.guild.GuildMember

/**
 * A scope interface that allows responding to user interactions with the bot, usually from application commands.
 */
interface InteractionScope {

    /**
     * The ID of the guild the interaction originated from. This will be null if the interaction originated from DMs.
     */
    val sourceGuildId: String?

    /**
     * The ID of the channel the interaction originated from.
     */
    val sourceChannelId: String?

    /**
     * The [GuildMember] this interaction originated from, or null if it did not come from a guild.
     */
    val sourceGuildMember: GuildMember?

    /**
     * Writes a basic message in response to an interaction.
     * @param ephemeral Whether the message is "ephemeral". An ephemeral message is a message only the user who
     * initiated the interaction can see. See the
     * [Discord documentation](https://support.discord.com/hc/en-us/articles/1500000580222-Ephemeral-Messages-FAQ) for
     * more.
     * @param content The text content for the message.
     */
    suspend fun createResponseMessage(ephemeral: Boolean, content: String)

    /**
     * Retrieves the channel ID of a channel option with the given name.
     */
    fun getChannelId(optionName: String): String
}
