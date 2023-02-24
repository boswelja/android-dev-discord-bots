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
package jda.channel

import channel.EmbedBuilder
import channel.MessageScope
import kotlinx.datetime.Instant
import kotlinx.datetime.toJavaInstant
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel
import java.time.ZoneOffset

class JdaMessageScope(private val jda: JDA): MessageScope {
    override suspend fun createEmbed(targetChannelId: String, builder: EmbedBuilder.() -> Unit) {
        val channel = jda.getChannelById(MessageChannel::class.java, targetChannelId)
        requireNotNull(channel) { "Channel with ID $targetChannelId is not a valid message channel" }
        channel.sendMessageEmbeds(
            listOf(
                JdaEmbedBuilder().apply(builder).build()
            )
        )
    }
}

internal class JdaEmbedBuilder : EmbedBuilder {

    private var thumbnail: MessageEmbed.Thumbnail? = null
    private var footer: MessageEmbed.Footer? = null
    private var author: MessageEmbed.AuthorInfo? = null

    override var title: String? = null
    override var description: String? = null
    override var url: String? = null
    override var timestamp: Instant? = null
    override var color: Int? = null
    override var imageUrl: String? = null

    override fun footer(text: String, iconUrl: String?) {
        footer = MessageEmbed.Footer(text, iconUrl, null)
    }

    override fun thumbnail(url: String) {
        thumbnail = MessageEmbed.Thumbnail(url, null, 0, 0)
    }

    override fun author(name: String, url: String?, iconUrl: String?) {
        author = MessageEmbed.AuthorInfo(name, url, iconUrl, null)
    }

    fun build(): MessageEmbed {
        return MessageEmbed(
            url,
            title,
            description,
            null,
            timestamp?.toJavaInstant()?.atOffset(ZoneOffset.UTC),
            color ?: 0,
            thumbnail,
            null,
            author,
            null,
            footer,
            null,
            null
        )
    }
}
