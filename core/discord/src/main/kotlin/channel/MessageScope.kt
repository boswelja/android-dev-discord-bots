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
 * A scope interface for managing messages.
 */
interface MessageScope {

    /**
     * Creates a new embed message.
     * @param targetChannelId The ID of the channel to write the message to.
     * @param builder The embed builder. See [EmbedBuilder].
     */
    suspend fun createEmbed(targetChannelId: String, builder: EmbedBuilder.() -> Unit)

    /**
     * Creates a new post in a forum channel.
     * @param targetChannelId The channel to create the thread in.
     * @param name 1-100 character channel name.
     * @param appliedTags The IDs of the set of tags to be applied to the new thread.
     * @param builder The embed builder. See [EmbedBuilder].
     */
    suspend fun createForumPost(
        targetChannelId: String,
        name: String,
        appliedTags: Set<String>? = null,
        builder: EmbedBuilder.() -> Unit,
    )
}
