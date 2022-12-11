import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
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
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.DurationUnit

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
                assertEquals(1.0, currentTime.milliseconds.toDouble(DurationUnit.HOURS))
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
                assertEquals(1.0, currentTime.milliseconds.toDouble(DurationUnit.HOURS))
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
                assertEquals(1.0, currentTime.milliseconds.toDouble(DurationUnit.HOURS))
                cancel()
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `schedule daily repeats at 24 hour intervals`() = runTest {
        // Wrap in a Launch, so we can cancel without throwing exceptions in this scope
        launch {
            var runCount = 0
            var lastRunTime = 0L
            schedule(
                repeating = Repeating.Daily(),
                clock = testClock
            ) {
                val millisSinceLastRun = currentTime - lastRunTime

                // Advance the test clock by elapsed time
                testClock.advanceTimeBy(millisSinceLastRun)
                lastRunTime = currentTime

                if (runCount > 5) {
                    // If we've done more than 5 runs, cancel
                    cancel()
                } else if (runCount > 0) {
                    // Else if we have passed one run, start asserting
                    val daysElapsed = millisSinceLastRun.milliseconds.toDouble(DurationUnit.DAYS)
                    println(daysElapsed)
                    assertEquals(1.0, daysElapsed)
                }

                // Increment run count
                runCount++
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `schedule weekly repeats at 7 day intervals`() = runTest {
        // Wrap in a Launch, so we can cancel without throwing exceptions in this scope
        launch {
            var runCount = 0
            var lastRunTime = 0L
            schedule(
                repeating = Repeating.Weekly(DayOfWeek.MONDAY),
                clock = testClock
            ) {
                val millisSinceLastRun = currentTime - lastRunTime

                // Advance the test clock by elapsed time
                testClock.advanceTimeBy(millisSinceLastRun)
                lastRunTime = currentTime

                if (runCount > 5) {
                    // If we've done more than 5 runs, cancel
                    cancel()
                } else if (runCount > 0) {
                    // Else if we have passed one run, start asserting
                    val daysElapsed = millisSinceLastRun.milliseconds.toDouble(DurationUnit.DAYS)
                    println(daysElapsed)
                    assertEquals(7.0, daysElapsed)
                }

                // Increment run count
                runCount++
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `schedule fortnightly repeats at 14 day intervals`() = runTest {
        // Wrap in a Launch, so we can cancel without throwing exceptions in this scope
        launch {
            var runCount = 0
            var lastRunTime = 0L
            schedule(
                repeating = Repeating.Fortnightly(DayOfWeek.MONDAY),
                clock = testClock
            ) {
                val millisSinceLastRun = currentTime - lastRunTime

                // Advance the test clock by elapsed time
                testClock.advanceTimeBy(millisSinceLastRun)
                lastRunTime = currentTime

                if (runCount > 5) {
                    // If we've done more than 5 runs, cancel
                    cancel()
                } else if (runCount > 0) {
                    // Else if we have passed one run, start asserting
                    val daysElapsed = millisSinceLastRun.milliseconds.toDouble(DurationUnit.DAYS)
                    println(daysElapsed)
                    assertEquals(14.0, daysElapsed)
                }

                // Increment run count
                runCount++
            }
        }
    }
}
