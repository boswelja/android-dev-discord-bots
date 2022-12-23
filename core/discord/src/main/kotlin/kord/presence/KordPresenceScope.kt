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
package kord.presence

import dev.kord.gateway.Gateway
import dev.kord.gateway.start
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import presence.PresenceScope
import presence.PresenceStatus

class KordPresenceScope(
    private val token: String,
    private val gateway: Gateway,
    private val scope: CoroutineScope,
) : PresenceScope {
    override fun updatePresence(afk: Boolean, status: PresenceStatus) {
        scope.launch {
            gateway.stop()
            gateway.start(token) {
                presence {
                    this.afk = afk
                    this.status = when (status) {
                        PresenceStatus.INVISIBLE -> dev.kord.common.entity.PresenceStatus.Invisible
                        PresenceStatus.DO_NOT_DISTURB -> dev.kord.common.entity.PresenceStatus.DoNotDisturb
                        PresenceStatus.IDLE -> dev.kord.common.entity.PresenceStatus.Idle
                        PresenceStatus.ONLINE -> dev.kord.common.entity.PresenceStatus.Online
                    }
                }
            }
        }
    }
}
