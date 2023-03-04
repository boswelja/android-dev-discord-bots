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
package discord.jda.channel

import discord.channel.Channel
import discord.channel.ChannelScope
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.channel.ChannelType
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel

internal class JdaChannelScope(
    private val jda: JDA,
) : ChannelScope {
    override suspend fun getChannel(channelId: String): Channel {
        val channel = jda.getChannelById(GuildChannel::class.java, channelId)
        requireNotNull(channel) { "No channel found with ID $channelId" }
        return Channel(
            type = when (channel.type) {
                ChannelType.TEXT -> Channel.Type.GUILD_TEXT
                ChannelType.PRIVATE -> Channel.Type.DM
                ChannelType.VOICE -> Channel.Type.GUILD_VOICE
                ChannelType.GROUP -> Channel.Type.GROUP_DM
                ChannelType.CATEGORY -> Channel.Type.GUILD_CATEGORY
                ChannelType.NEWS -> Channel.Type.GUILD_ANNOUNCEMENT
                ChannelType.STAGE -> Channel.Type.GUILD_STAGE_VOICE
                ChannelType.GUILD_NEWS_THREAD -> Channel.Type.ANNOUNCEMENT_THREAD
                ChannelType.GUILD_PUBLIC_THREAD -> Channel.Type.PUBLIC_THREAD
                ChannelType.GUILD_PRIVATE_THREAD -> Channel.Type.PRIVATE_THREAD
                ChannelType.FORUM -> Channel.Type.GUILD_FORUM
                ChannelType.UNKNOWN -> error("Unknown channel type for channel with ID $channelId")
            },
            name = channel.name,
            topic = (channel as? TextChannel)?.topic,
            nsfw = (channel as? TextChannel)?.isNSFW ?: false,
            lastMessageId = (channel as? MessageChannel)?.latestMessageId,
        )
    }
}
