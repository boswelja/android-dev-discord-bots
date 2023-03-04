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
package discord.guild

import kotlinx.datetime.Instant

/**
 * A member of a guild (a.k.a server).
 *
 * @property joinedAt When the user joined the guild.
 * @property permissions A set of [MemberPermission]s this member has in the guild.
 * @property nickname The members nickname, or null if they don't have one set.
 * @property avatarUrl The URL of the members guild avatar, or null if they don't have a custom avatar set.
 * @property boostingSince When the member started boosting the guild.
 * @property timedOutUntil When the user will be out of their timeout.
 */
data class GuildMember(
    val joinedAt: Instant,
    val permissions: Set<MemberPermission>,
    val nickname: String?,
    val avatarUrl: String?,
    val boostingSince: Instant?,
    val timedOutUntil: Instant?,
)
