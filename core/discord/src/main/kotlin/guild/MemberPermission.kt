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
package guild

enum class MemberPermission {
    // General Server / Channel Permissions
    MANAGE_CHANNEL,
    VIEW_AUDIT_LOGS,
    VIEW_CHANNEL,
    VIEW_GUILD_INSIGHTS,
    MANAGE_ROLES,
    MANAGE_PERMISSIONS,
    MANAGE_WEBHOOKS,
    MANAGE_EMOJIS_AND_STICKERS,
    MANAGE_EVENTS,
    CREATE_INSTANT_INVITE,
    KICK_MEMBERS,
    BAN_MEMBERS,
    NICKNAME_CHANGE,
    NICKNAME_MANAGE,
    MODERATE_MEMBERS,
    MESSAGE_ADD_REACTION,
    MESSAGE_SEND,
    MESSAGE_TTS,
    MESSAGE_MANAGE,
    MESSAGE_EMBED_LINKS,
    MESSAGE_ATTACH_FILES,
    MESSAGE_HISTORY,
    MESSAGE_MENTION_EVERYONE,
    MESSAGE_EXT_EMOJI,
    USE_APPLICATION_COMMANDS,
    MESSAGE_EXT_STICKER,
    MANAGE_THREADS,
    CREATE_PUBLIC_THREADS,
    CREATE_PRIVATE_THREADS,
    MESSAGE_SEND_IN_THREADS,
    PRIORITY_SPEAKER,
    VOICE_STREAM,
    VOICE_CONNECT,
    VOICE_SPEAK,
    VOICE_MUTE_OTHERS,
    VOICE_DEAF_OTHERS,
    VOICE_MOVE_OTHERS,
    VOICE_USE_VAD,
    VOICE_START_ACTIVITIES,
    REQUEST_TO_SPEAK,
    ADMINISTRATOR,
    UNKNOWN,
}
