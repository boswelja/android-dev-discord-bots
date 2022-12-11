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
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

@OptIn(ExperimentalTime::class)
suspend fun schedule(
    repeating: Repeating,
    clock: Clock = Clock.System,
    block: suspend () -> Unit
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
