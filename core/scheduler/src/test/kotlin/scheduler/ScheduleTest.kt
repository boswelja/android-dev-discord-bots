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

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours

class ScheduleTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `scheduleRepeating one hour in the future executes in one hour`() = runTest {
        // Wrap in a Launch, so we can cancel after the first execution
        var executions = 0
        val scheduleJob = launch {
            scheduleRepeating(
                interval = 7.days,
                delayUntilStart = 1.hours,
            ) {
                executions++
            }
        }

        advanceTimeBy(1.hours.inWholeMilliseconds + 1)
        assertEquals(1, executions)
        scheduleJob.cancel()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `scheduleRepeating daily repeats at 24 hour intervals`() = runTest {
        // Wrap in a Launch, so we can cancel without throwing exceptions in this scope
        launch {
            testScheduleRepeating(
                interval = 1.days,
                delayUntilStart = Duration.ZERO,
            )
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `scheduleRepeating weekly repeats at 7 day intervals`() = runTest {
        // Wrap in a Launch, so we can cancel without throwing exceptions in this scope
        launch {
            testScheduleRepeating(
                interval = 7.days,
                delayUntilStart = Duration.ZERO,
            )
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `scheduleRepeating fortnightly repeats at 14 day intervals`() = runTest {
        // Wrap in a Launch, so we can cancel without throwing exceptions in this scope
        launch {
            testScheduleRepeating(
                interval = 14.days,
                delayUntilStart = Duration.ZERO,
            )
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun TestScope.testScheduleRepeating(
        interval: Duration,
        delayUntilStart: Duration,
        targetRuns: Int = 5,
    ) {
        // Wrap in a Launch, so we can cancel after the first execution
        var executions = 0
        val scheduleJob = launch {
            scheduleRepeating(
                interval = interval,
                delayUntilStart = delayUntilStart,
            ) {
                executions++
            }
        }

        // -1 here because scheduler executes block immediately
        advanceTimeBy(interval.inWholeMilliseconds * (targetRuns - 1))
        assertEquals(targetRuns, executions)
        scheduleJob.cancel()
    }
}
