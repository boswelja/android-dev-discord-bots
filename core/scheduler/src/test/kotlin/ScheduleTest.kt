import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.currentTime
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.junit.jupiter.api.Test
import java.time.DayOfWeek
import kotlin.test.assertTrue
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

class ScheduleTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `schedule daily one hour in the future executes in one hour`() = runTest {
        val nowTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

        // Wrap in a Launch, so we can cancel after the first execution
        launch {
            schedule(
                repeating = Repeating.Daily(nowTime.hour + 1, nowTime.minute, nowTime.second)
            ) {
                assertEqualsApproximately(
                    1.hours,
                    currentTime.milliseconds,
                    1.seconds
                )
                cancel()
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `schedule weekly one hour in the future executes in one hour`() = runTest {
        val nowTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

        // Wrap in a Launch, so we can cancel after the first execution
        launch {
            schedule(
                repeating = Repeating.Weekly(nowTime.dayOfWeek, nowTime.hour + 1, nowTime.minute, nowTime.second)
            ) {
                assertEqualsApproximately(
                    1.hours,
                    currentTime.milliseconds,
                    1.seconds
                )
                cancel()
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `schedule fortnightly one hour in the future executes in one hour`() = runTest {
        val nowTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

        // Wrap in a Launch, so we can cancel after the first execution
        launch {
            schedule(
                repeating = Repeating.Fortnightly(nowTime.dayOfWeek, nowTime.hour + 1, nowTime.minute, nowTime.second)
            ) {
                assertEqualsApproximately(
                    1.hours,
                    currentTime.milliseconds,
                    1.seconds
                )
                cancel()
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `schedule daily repeats at 24 hour intervals`() = runTest {
        // Wrap in a Launch, so we can cancel without throwing exceptions in this scope
        launch {
            testScheduleRepeating(
                repeating = Repeating.Daily()
            )
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `schedule weekly repeats at 7 day intervals`() = runTest {
        // Wrap in a Launch, so we can cancel without throwing exceptions in this scope
        launch {
            testScheduleRepeating(
                repeating = Repeating.Weekly(DayOfWeek.MONDAY)
            )
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `schedule fortnightly repeats at 14 day intervals`() = runTest {
        // Wrap in a Launch, so we can cancel without throwing exceptions in this scope
        launch {
            testScheduleRepeating(
                repeating = Repeating.Fortnightly(DayOfWeek.MONDAY)
            )
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun TestScope.testScheduleRepeating(
        repeating: Repeating
    ) {
        var runCount = 0
        var lastRunTime = 0L
        schedule(
            repeating = repeating
        ) {
            val millisSinceLastRun = currentTime - lastRunTime

            if (runCount > 5) {
                // If we've done more than 5 runs, cancel
                coroutineScope { cancel() }
            } else if (runCount > 0) {
                // Else if we have passed one run, start asserting
                assertEqualsApproximately(
                    repeating.interval,
                    millisSinceLastRun.milliseconds,
                    1.seconds
                )
            }

            // Increment run count
            runCount++
            lastRunTime = currentTime
        }
    }

    private fun assertEqualsApproximately(expected: Duration, actual: Duration, tolerance: Duration) {
        val range = (expected - tolerance)..(expected + tolerance)
        assertTrue(range.contains(actual), "Expected duration within $tolerance of $expected, but got $actual")
    }
}
