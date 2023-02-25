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
package jda.interaction

import interaction.InteractionScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.dv8tion.jda.api.events.interaction.command.GenericCommandInteractionEvent

internal class JdaInteractionScope(private val event: GenericCommandInteractionEvent) : InteractionScope {

    override val sourceGuildId: String? = event.guild?.id
    override val sourceChannelId: String? = event.channel?.id

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
