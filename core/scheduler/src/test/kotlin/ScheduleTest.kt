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
        val nowTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

        // Wrap in a Launch, so we can cancel after the first execution
        var executions = 0
        val scheduleJob = launch {
            schedule(
                repeating = Repeating.Daily(nowTime.hour + 1, nowTime.minute, nowTime.second)
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
        val nowTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

        // Wrap in a Launch, so we can cancel after the first execution
        var executions = 0
        val scheduleJob = launch {
            schedule(
                repeating = Repeating.Weekly(nowTime.dayOfWeek, nowTime.hour + 1, nowTime.minute, nowTime.second)
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
        val nowTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

        // Wrap in a Launch, so we can cancel after the first execution
        var executions = 0
        val scheduleJob = launch {
            schedule(
                repeating = Repeating.Fortnightly(nowTime.dayOfWeek, nowTime.hour + 1, nowTime.minute, nowTime.second)
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
    private fun TestScope.testScheduleRepeating(
        repeating: Repeating,
        targetRuns: Int = 5
    ) {
        // Wrap in a Launch, so we can cancel after the first execution
        var executions = 0
        val scheduleJob = launch {
            schedule(
                repeating = repeating
            ) {
                executions++
            }
        }

        advanceTimeBy(repeating.interval.inWholeMilliseconds * targetRuns)
        assertEquals(targetRuns, executions)
        scheduleJob.cancel()
    }
}
