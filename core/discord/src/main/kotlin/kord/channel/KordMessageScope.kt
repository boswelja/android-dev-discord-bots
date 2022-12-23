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
package kord.channel

import channel.EmbedBuilder
import channel.MessageScope
import dev.kord.common.Color
import dev.kord.common.entity.Snowflake
import dev.kord.rest.builder.message.create.embed
import dev.kord.rest.service.RestClient
import kotlinx.datetime.Instant

internal class KordMessageScope(
    private val restClient: RestClient,
) : MessageScope {
    override suspend fun createEmbed(targetChannelId: String, builder: EmbedBuilder.() -> Unit) {
        restClient.channel.createMessage(Snowflake(targetChannelId)) {
            embed {
                // Create our own EmbedBuilder that stores data in Kord's embed builder
                val embedBuilder = object : EmbedBuilder {
                    override var title: String? by this@embed::title
                    override var description: String? by this@embed::description
                    override var url: String? by this@embed::url
                    override var timestamp: Instant? by this@embed::timestamp
                    override var imageUrl: String? by this@embed::image
                    override var color: Int?
                        get() = this@embed.color?.rgb
                        set(value) { this@embed.color = value?.let { Color(it) } }

                    override fun footer(text: String, iconUrl: String?) = footer {
                        this.text = text
                        this.icon = iconUrl
                    }

                    override fun thumbnail(url: String) = thumbnail {
                        this.url = url
                    }

                    override fun author(name: String, url: String?, iconUrl: String?) = author {
                        this.url = url
                        this.name = name
                        this.icon = iconUrl
                    }
                }
                // Once we call this, our Kord embed builder should have data so there's nothing else to do
                embedBuilder.apply(builder)
            }
        }
    }
}
