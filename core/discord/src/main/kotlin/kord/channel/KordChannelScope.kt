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
package kord.channel

import channel.Channel
import channel.ChannelScope
import dev.kord.common.entity.ChannelType
import dev.kord.common.entity.Snowflake
import dev.kord.common.entity.optional.value
import dev.kord.rest.service.RestClient

internal class KordChannelScope(
    private val restClient: RestClient,
) : ChannelScope {
    override suspend fun getChannel(channelId: String): Channel {
        val kordChannel = restClient.channel.getChannel(Snowflake(channelId))
        return Channel(
            type = when (kordChannel.type) {
                ChannelType.DM -> Channel.Type.DM
                ChannelType.GroupDM -> Channel.Type.GROUP_DM
                ChannelType.GuildCategory -> Channel.Type.GUILD_CATEGORY
                ChannelType.GuildDirectory -> Channel.Type.GUILD_DIRECTORY
                ChannelType.GuildForum -> Channel.Type.GUILD_FORUM
                ChannelType.GuildNews -> Channel.Type.GUILD_ANNOUNCEMENT
                ChannelType.GuildStageVoice -> Channel.Type.GUILD_STAGE_VOICE
                ChannelType.GuildText -> Channel.Type.GUILD_TEXT
                ChannelType.GuildVoice -> Channel.Type.GUILD_VOICE
                ChannelType.PrivateThread -> Channel.Type.PRIVATE_THREAD
                ChannelType.PublicGuildThread -> Channel.Type.PUBLIC_THREAD
                ChannelType.PublicNewsThread -> Channel.Type.ANNOUNCEMENT_THREAD
                is ChannelType.Unknown -> error("Unknown channel type for channel with ID $channelId")
            },
            position = kordChannel.position.value,
            name = kordChannel.name.value,
            topic = kordChannel.topic.value,
            lastMessageId = kordChannel.lastMessageId.value?.toString(),
            nsfw = kordChannel.nsfw.value == true,
        )
    }
}
