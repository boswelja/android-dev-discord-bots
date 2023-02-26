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
package discord.presence

/**
 * A scope interface for managing bot presence. Presence includes things like online status, now playing details etc.
 */
interface PresenceScope {

    /**
     * Updates the bots presence to match the given state.
     * @param afk Whether the bot should be displayed as AFK.
     * @param status The status that should be displayed for the bot.
     */
    fun updatePresence(afk: Boolean, status: PresenceStatus)
}

/**
 * User statuses available as part of the "presence" system.
 */
enum class PresenceStatus {
    INVISIBLE,
    DO_NOT_DISTURB,
    IDLE,
    ONLINE,
}