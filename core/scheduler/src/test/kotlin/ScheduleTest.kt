import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.currentTime
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.junit.jupiter.api.Test
import kotlin.test.assertContains
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.DurationUnit

class ScheduleTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `schedule daily one hour in the future executes in one hour`() = runTest {
        val nowTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

        // Wrap in a Launch, so we can cancel after the first execution
        launch {
            schedule(Repeating.Daily(nowTime.hour + 1, nowTime.minute, nowTime.second)) {
                assertContains(0.9..1.1, currentTime.milliseconds.toDouble(DurationUnit.HOURS))
                cancel()
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `schedule daily repeats at 24 hour intervals`() = runTest {
        // Wrap in a Launch, so we can cancel after the first execution
        launch {
            var runCount = 0
            var lastRunTime = 0L
            schedule(Repeating.Daily()) {
                if (runCount == 0) {
                    lastRunTime = currentTime
                } else if (runCount > 5) {
                    cancel()
                } else {
                    assertContains(23.9..24.1, (currentTime - lastRunTime).milliseconds.toDouble(DurationUnit.HOURS))
                    lastRunTime = currentTime
                }
                runCount++
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `schedule weekly one hour in the future executes in one hour`() = runTest {
        val nowTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

        // Wrap in a Launch, so we can cancel after the first execution
        launch {
            schedule(Repeating.Weekly(nowTime.dayOfWeek, nowTime.hour + 1, nowTime.minute, nowTime.second)) {
                assertContains(0.9..1.1, currentTime.milliseconds.toDouble(DurationUnit.HOURS))
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
            schedule(Repeating.Fortnightly(nowTime.dayOfWeek, nowTime.hour + 1, nowTime.minute, nowTime.second)) {
                assertContains(0.9..1.1, currentTime.milliseconds.toDouble(DurationUnit.HOURS))
                cancel()
            }
        }
    }
}
