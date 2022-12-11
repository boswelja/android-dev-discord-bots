import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.currentTime
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.junit.jupiter.api.Test
import java.time.DayOfWeek
import kotlin.test.BeforeTest
import kotlin.test.assertContains
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

class ScheduleTest {

    private val testClock = object : Clock {
        private var instant = Clock.System.now()

        override fun now(): Instant  = instant

        fun resetClock() {
            instant = Clock.System.now()
        }

        fun advanceTimeBy(millis: Long) {
            instant += millis.milliseconds
        }
    }

    @BeforeTest
    fun setUp() {
        testClock.resetClock()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `schedule daily one hour in the future executes in one hour`() = runTest {
        val nowTime = testClock.now().toLocalDateTime(TimeZone.currentSystemDefault())

        // Wrap in a Launch, so we can cancel after the first execution
        launch {
            schedule(
                repeating = Repeating.Daily(nowTime.hour + 1, nowTime.minute, nowTime.second),
                clock = testClock
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
        val nowTime = testClock.now().toLocalDateTime(TimeZone.currentSystemDefault())

        // Wrap in a Launch, so we can cancel after the first execution
        launch {
            schedule(
                repeating = Repeating.Weekly(nowTime.dayOfWeek, nowTime.hour + 1, nowTime.minute, nowTime.second),
                clock = testClock
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
        val nowTime = testClock.now().toLocalDateTime(TimeZone.currentSystemDefault())

        // Wrap in a Launch, so we can cancel after the first execution
        launch {
            schedule(
                repeating = Repeating.Fortnightly(nowTime.dayOfWeek, nowTime.hour + 1, nowTime.minute, nowTime.second),
                clock = testClock
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
                repeating = Repeating.Daily(),
                expectedDurationBetweenRuns = 1.days
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
                expectedDurationBetweenRuns = 7.days
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
                expectedDurationBetweenRuns = 14.days
            )
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun TestScope.testScheduleRepeating(
        repeating: Repeating,
        expectedDurationBetweenRuns: Duration
    ) {
        var runCount = 0
        var lastRunTime = 0L
        schedule(
            repeating = repeating,
            clock = testClock
        ) {
            val millisSinceLastRun = currentTime - lastRunTime

            // Advance the test clock by elapsed time
            testClock.advanceTimeBy(millisSinceLastRun)
            lastRunTime = currentTime

            if (runCount > 5) {
                // If we've done more than 5 runs, cancel
                coroutineScope { cancel() }
            } else if (runCount > 0) {
                // Else if we have passed one run, start asserting
                assertEqualsApproximately(
                    expectedDurationBetweenRuns,
                    millisSinceLastRun.milliseconds,
                    1.seconds
                )
            }

            // Increment run count
            runCount++
        }
    }

    private fun assertEqualsApproximately(expected: Duration, actual: Duration, tolerance: Duration) {
        assertContains(
            (expected - tolerance)..(expected + tolerance),
            actual
        )
    }
}
