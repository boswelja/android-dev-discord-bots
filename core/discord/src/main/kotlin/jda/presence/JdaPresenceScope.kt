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
package jda.presence

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.OnlineStatus
import presence.PresenceScope
import presence.PresenceStatus

class JdaPresenceScope(private val jda: JDA) : PresenceScope {
    override fun updatePresence(afk: Boolean, status: PresenceStatus) {
        jda.presence.setPresence(
            when (status) {
                PresenceStatus.INVISIBLE -> OnlineStatus.INVISIBLE
                PresenceStatus.DO_NOT_DISTURB -> OnlineStatus.DO_NOT_DISTURB
                PresenceStatus.IDLE -> OnlineStatus.IDLE
                PresenceStatus.ONLINE -> OnlineStatus.ONLINE
            },
            afk
        )
    }
}
