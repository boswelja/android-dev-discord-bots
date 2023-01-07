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
package channel

/**
 * A scope interface for interacting with Discord channels.
 */
interface ChannelScope {

    /**
     * Gets a [Channel] for the given channel ID.
     */
    suspend fun getChannel(channelId: String): Channel
}

/**
 * Represents a Discord Channel.
 * @property type The type of the channel.
 * @property position The order of the channel in the channel list, or null if there is no order.
 * @property name The name of the channel, or null if there is no name.
 * @property topic The topic of the channel, or null if there is no topic.
 * @property nsfw Whether this is a NSFW channel.
 * @property lastMessageId The ID of the last message sent. This may or may not be a valid message.
 */
data class Channel(
    val type: Type,
    val position: Int?,
    val name: String?,
    val topic: String?,
    val nsfw: Boolean,
    val lastMessageId: String?,
) {
    enum class Type {
        GUILD_TEXT,
        DM,
        GUILD_VOICE,
        GROUP_DM,
        GUILD_CATEGORY,
        GUILD_ANNOUNCEMENT,
        ANNOUNCEMENT_THREAD,
        PUBLIC_THREAD,
        PRIVATE_THREAD,
        GUILD_STAGE_VOICE,
        GUILD_DIRECTORY,
        GUILD_FORUM,
    }
}
