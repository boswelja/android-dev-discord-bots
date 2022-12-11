import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atTime
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.coroutines.coroutineContext
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days

suspend fun schedule(
    repeating: Repeating,
    clock: Clock = Clock.System,
    block: suspend () -> Unit
) {
    while (coroutineContext.isActive) {
        delay(calculateDelayUntilNextExecution(clock, repeating))
        block()
    }
}

private fun calculateDelayUntilNextExecution(clock: Clock, repeating: Repeating): Duration {
    val timeZone = TimeZone.currentSystemDefault()
    val nowDateTime = clock.now().toLocalDateTime(timeZone)

    val nextDateTime = nowDateTime.date
        .atTime(repeating.hour, repeating.minute, repeating.second)

    val nextInstant = if (nextDateTime <= nowDateTime) {
        val extraDays = when (repeating) {
            is Repeating.Daily -> 1
            is Repeating.Weekly -> 7
            is Repeating.Fortnightly -> 14
        }
        nextDateTime.toInstant(timeZone)
            .plus(extraDays.days)
    } else {
        nextDateTime.toInstant(timeZone)
    }
    return nextInstant - nowDateTime.toInstant(timeZone)
}
