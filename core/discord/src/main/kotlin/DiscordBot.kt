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
import channel.MessageScope
import dev.kord.common.entity.PresenceStatus
import dev.kord.gateway.DefaultGateway
import dev.kord.gateway.start
import dev.kord.rest.service.RestClient
import interaction.ApplicationCommandScope
import kord.channel.KordMessageScope
import kord.interaction.KordApplicationCommandScope
import kord.presence.KordPresenceScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import presence.PresenceScope
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

@OptIn(ExperimentalContracts::class)
suspend inline fun discordBot(
    token: String,
    crossinline block: suspend DiscordBotScope.() -> Unit
) {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }

    supervisorScope {
        val bot = createKordDiscordBot(token)

        launch {
            block(bot)
        }
    }
}

class DiscordBotScope(
    private val applicationCommandScope: ApplicationCommandScope,
    private val messageScope: MessageScope,
    private val presenceScope: PresenceScope
) : ApplicationCommandScope by applicationCommandScope,
    MessageScope by messageScope,
    PresenceScope by presenceScope

fun CoroutineScope.createKordDiscordBot(token: String): DiscordBotScope {
    val restClient = RestClient(token)
    val gateway = DefaultGateway()

    // Using GlobalScope so we don't block anything here
    launch {
        gateway.start(token) {
            presence {
                status = PresenceStatus.Online
            }
        }
    }
    return DiscordBotScope(
        KordApplicationCommandScope(restClient, gateway),
        KordMessageScope(restClient),
        KordPresenceScope(token, gateway, this)
    )
}
