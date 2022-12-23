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
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atTime
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.coroutines.coroutineContext
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

/**
 * Schedules repeating work via Coroutines. Cancelling the parent coroutine will cancel subsequent work appropriately.
 * See [Repeating] for possible configuration options.
 */
@OptIn(ExperimentalTime::class)
suspend fun schedule(
    repeating: Repeating,
    clock: Clock = Clock.System,
    block: suspend () -> Unit,
) {
    delay(calculateInitialDelay(clock, repeating))
    while (coroutineContext.isActive) {
        val workDuration = measureTime {
            block()
        }
        delay(repeating.interval - workDuration)
    }
}

private fun calculateInitialDelay(clock: Clock, repeating: Repeating): Duration {
    val timeZone = TimeZone.currentSystemDefault()
    val nowDateTime = clock.now().toLocalDateTime(timeZone)

    val nextDateTime = nowDateTime.date
        .atTime(repeating.hour, repeating.minute, repeating.second)

    val nextInstant = if (nextDateTime <= nowDateTime) {
        nextDateTime.toInstant(timeZone) + repeating.interval
    } else {
        nextDateTime.toInstant(timeZone)
    }
    return nextInstant - nowDateTime.toInstant(timeZone)
}
