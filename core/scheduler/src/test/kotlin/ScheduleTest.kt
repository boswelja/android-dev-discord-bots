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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.junit.jupiter.api.Test
import java.time.DayOfWeek
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.hours

class ScheduleTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `schedule daily one hour in the future executes in one hour`() = runTest {
        val nowTime = Clock.System.now()
        val targetTime = nowTime.plus(1.hours).toLocalDateTime(TimeZone.currentSystemDefault())

        // Wrap in a Launch, so we can cancel after the first execution
        var executions = 0
        val scheduleJob = launch {
            schedule(
                repeating = Repeating.Daily(targetTime.hour, targetTime.minute, targetTime.second),
            ) {
                executions++
            }
        }

        advanceTimeBy(1.hours.inWholeMilliseconds)
        assertEquals(1, executions)
        scheduleJob.cancel()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `schedule weekly one hour in the future executes in one hour`() = runTest {
        val nowTime = Clock.System.now()
        val targetTime = nowTime.plus(1.hours).toLocalDateTime(TimeZone.currentSystemDefault())

        // Wrap in a Launch, so we can cancel after the first execution
        var executions = 0
        val scheduleJob = launch {
            schedule(
                repeating = Repeating.Weekly(
                    targetTime.dayOfWeek,
                    targetTime.hour,
                    targetTime.minute,
                    targetTime.second,
                ),
            ) {
                executions++
            }
        }

        advanceTimeBy(1.hours.inWholeMilliseconds)
        assertEquals(1, executions)
        scheduleJob.cancel()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `schedule fortnightly one hour in the future executes in one hour`() = runTest {
        val nowTime = Clock.System.now()
        val targetTime = nowTime.plus(1.hours).toLocalDateTime(TimeZone.currentSystemDefault())

        // Wrap in a Launch, so we can cancel after the first execution
        var executions = 0
        val scheduleJob = launch {
            schedule(
                repeating = Repeating.Fortnightly(
                    targetTime.dayOfWeek,
                    targetTime.hour,
                    targetTime.minute,
                    targetTime.second,
                ),
            ) {
                executions++
            }
        }

        advanceTimeBy(1.hours.inWholeMilliseconds)
        assertEquals(1, executions)
        scheduleJob.cancel()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `schedule daily repeats at 24 hour intervals`() = runTest {
        // Wrap in a Launch, so we can cancel without throwing exceptions in this scope
        launch {
            testScheduleRepeating(
                repeating = Repeating.Daily(),
            )
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `schedule weekly repeats at 7 day intervals`() = runTest {
        // Wrap in a Launch, so we can cancel without throwing exceptions in this scope
        launch {
            testScheduleRepeating(
                repeating = Repeating.Weekly(DayOfWeek.MONDAY),
            )
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `schedule fortnightly repeats at 14 day intervals`() = runTest {
        // Wrap in a Launch, so we can cancel without throwing exceptions in this scope
        launch {
            testScheduleRepeating(
                repeating = Repeating.Fortnightly(DayOfWeek.MONDAY),
            )
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun TestScope.testScheduleRepeating(
        repeating: Repeating,
        targetRuns: Int = 5,
    ) {
        // Wrap in a Launch, so we can cancel after the first execution
        var executions = 0
        val scheduleJob = launch {
            schedule(
                repeating = repeating,
            ) {
                executions++
            }
        }

        advanceTimeBy(repeating.interval.inWholeMilliseconds * targetRuns)
        assertEquals(targetRuns, executions)
        scheduleJob.cancel()
    }
}
