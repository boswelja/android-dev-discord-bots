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
package discord.jda.interaction

import discord.guild.GuildMember
import discord.guild.MemberPermission
import discord.interaction.InteractionScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.toKotlinInstant
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.interaction.command.GenericCommandInteractionEvent

internal class JdaInteractionScope(private val event: GenericCommandInteractionEvent) : InteractionScope {

    override val sourceGuildId: String? = event.guild?.id
    override val sourceChannelId: String? = event.channel?.id

    override val sourceGuildMember: GuildMember? = event.member?.let {
        GuildMember(
            joinedAt = it.timeJoined.toInstant().toKotlinInstant(),
            permissions = it.permissions.map { permission ->
                when (permission) {
                    Permission.MANAGE_CHANNEL -> MemberPermission.MANAGE_CHANNEL
                    Permission.MANAGE_SERVER -> MemberPermission.MANAGE_SERVER
                    Permission.VIEW_AUDIT_LOGS -> MemberPermission.VIEW_AUDIT_LOGS
                    Permission.VIEW_CHANNEL -> MemberPermission.VIEW_CHANNEL
                    Permission.VIEW_GUILD_INSIGHTS -> MemberPermission.VIEW_GUILD_INSIGHTS
                    Permission.MANAGE_ROLES -> MemberPermission.MANAGE_ROLES
                    Permission.MANAGE_PERMISSIONS -> MemberPermission.MANAGE_PERMISSIONS
                    Permission.MANAGE_WEBHOOKS -> MemberPermission.MANAGE_WEBHOOKS
                    Permission.MANAGE_EMOJIS_AND_STICKERS -> MemberPermission.MANAGE_EMOJIS_AND_STICKERS
                    Permission.MANAGE_EVENTS -> MemberPermission.MANAGE_EVENTS
                    Permission.CREATE_INSTANT_INVITE -> MemberPermission.CREATE_INSTANT_INVITE
                    Permission.KICK_MEMBERS -> MemberPermission.KICK_MEMBERS
                    Permission.BAN_MEMBERS -> MemberPermission.BAN_MEMBERS
                    Permission.NICKNAME_CHANGE -> MemberPermission.NICKNAME_CHANGE
                    Permission.NICKNAME_MANAGE -> MemberPermission.NICKNAME_MANAGE
                    Permission.MODERATE_MEMBERS -> MemberPermission.MODERATE_MEMBERS
                    Permission.MESSAGE_ADD_REACTION -> MemberPermission.MESSAGE_ADD_REACTION
                    Permission.MESSAGE_SEND -> MemberPermission.MESSAGE_SEND
                    Permission.MESSAGE_TTS -> MemberPermission.MESSAGE_TTS
                    Permission.MESSAGE_MANAGE -> MemberPermission.MESSAGE_MANAGE
                    Permission.MESSAGE_EMBED_LINKS -> MemberPermission.MESSAGE_EMBED_LINKS
                    Permission.MESSAGE_ATTACH_FILES -> MemberPermission.MESSAGE_ATTACH_FILES
                    Permission.MESSAGE_HISTORY -> MemberPermission.MESSAGE_HISTORY
                    Permission.MESSAGE_MENTION_EVERYONE -> MemberPermission.MESSAGE_MENTION_EVERYONE
                    Permission.MESSAGE_EXT_EMOJI -> MemberPermission.MESSAGE_EXT_EMOJI
                    Permission.USE_APPLICATION_COMMANDS -> MemberPermission.USE_APPLICATION_COMMANDS
                    Permission.MESSAGE_EXT_STICKER -> MemberPermission.MESSAGE_EXT_STICKER
                    Permission.MANAGE_THREADS -> MemberPermission.MANAGE_THREADS
                    Permission.CREATE_PUBLIC_THREADS -> MemberPermission.CREATE_PUBLIC_THREADS
                    Permission.CREATE_PRIVATE_THREADS -> MemberPermission.CREATE_PRIVATE_THREADS
                    Permission.MESSAGE_SEND_IN_THREADS -> MemberPermission.MESSAGE_SEND_IN_THREADS
                    Permission.PRIORITY_SPEAKER -> MemberPermission.PRIORITY_SPEAKER
                    Permission.VOICE_STREAM -> MemberPermission.VOICE_STREAM
                    Permission.VOICE_CONNECT -> MemberPermission.VOICE_CONNECT
                    Permission.VOICE_SPEAK -> MemberPermission.VOICE_SPEAK
                    Permission.VOICE_MUTE_OTHERS -> MemberPermission.VOICE_MUTE_OTHERS
                    Permission.VOICE_DEAF_OTHERS -> MemberPermission.VOICE_DEAF_OTHERS
                    Permission.VOICE_MOVE_OTHERS -> MemberPermission.VOICE_MOVE_OTHERS
                    Permission.VOICE_USE_VAD -> MemberPermission.VOICE_USE_VAD
                    Permission.VOICE_START_ACTIVITIES -> MemberPermission.VOICE_START_ACTIVITIES
                    Permission.REQUEST_TO_SPEAK -> MemberPermission.REQUEST_TO_SPEAK
                    Permission.ADMINISTRATOR -> MemberPermission.ADMINISTRATOR
                    Permission.MANAGE_GUILD_EXPRESSIONS -> MemberPermission.MANAGE_GUILD_EXPRESSIONS
                    Permission.VIEW_CREATOR_MONETIZATION_ANALYTICS ->
                        MemberPermission.VIEW_CREATOR_MONETIZATION_ANALYTICS
                    Permission.MESSAGE_ATTACH_VOICE_MESSAGE -> MemberPermission.MESSAGE_ATTACH_VOICE_MESSAGE
                    Permission.VOICE_USE_SOUNDBOARD -> MemberPermission.VOICE_USE_SOUNDBOARD
                    Permission.VOICE_USE_EXTERNAL_SOUNDS -> MemberPermission.VOICE_USE_EXTERNAL_SOUNDS
                    Permission.UNKNOWN,
                    null,
                    -> MemberPermission.UNKNOWN
                }
            }.toSet(),
            nickname = it.nickname,
            avatarUrl = it.avatarUrl,
            boostingSince = it.timeBoosted?.toInstant()?.toKotlinInstant(),
            timedOutUntil = it.timeOutEnd?.toInstant()?.toKotlinInstant(),
        )
    }

    override suspend fun createResponseMessage(ephemeral: Boolean, content: String) {
        withContext(Dispatchers.IO) {
            event.reply(content)
                .setEphemeral(ephemeral)
                .complete()
        }
    }

    override fun getChannelId(optionName: String): String {
        return event.getOption(optionName)?.asChannel?.id
            ?: error("No option with the name $optionName provided")
    }
}
