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
package channel

import kotlinx.datetime.Instant

/**
 * A builder for Discord message embeds.
 */
interface EmbedBuilder {

    /**
     * The title to display in the embed.
     */
    var title: String?

    /**
     * The description to display in the embed.
     */
    var description: String?

    /**
     * The content URL to display in the embed.
     */
    var url: String?

    /**
     * The timestamp to display in the embed.
     */
    var timestamp: Instant?

    /**
     * The optional RGB color to display the embed.
     */
    var color: Int?

    /**
     * An optional image URL to display in the embed.
     */
    var imageUrl: String?

    /**
     * Adds a footer to the embed. Only one footer can exist at a time.
     * @param text The text to display in the footer.
     * @param iconUrl An optional URL for an icon.
     */
    fun footer(text: String, iconUrl: String?)

    /**
     * Adds a thumbnail image to the embed.
     * @param url The URL of the thumbnail.
     */
    fun thumbnail(url: String)

    /**
     * Adds author information to the embed. Only one author block can exist at a time.
     * @param name The name of the author.
     * @param url An optional URL that may link to the author's bio or similar.
     * @param iconUrl An optional URL of an image representing the author.
     */
    fun author(name: String, url: String?, iconUrl: String?)
}
