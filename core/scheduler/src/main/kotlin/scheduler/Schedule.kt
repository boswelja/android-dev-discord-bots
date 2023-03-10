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
package scheduler

import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlin.coroutines.coroutineContext
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

/**
 * Schedules repeating work via Coroutines. Cancelling the parent coroutine will cancel subsequent work appropriately.
 */
@OptIn(ExperimentalTime::class)
suspend fun scheduleRepeating(
    interval: Duration,
    delayUntilStart: Duration = Duration.ZERO,
    block: suspend () -> Unit,
) {
    delay(delayUntilStart)
    while (coroutineContext.isActive) {
        val workDuration = measureTime {
            block()
        }
        delay(interval - workDuration)
    }
}
