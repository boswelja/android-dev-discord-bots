import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toKotlinLocalDateTime
import kotlinx.datetime.toLocalDateTime
import kotlin.coroutines.coroutineContext
import kotlin.time.Duration

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

    // TODO Don't convert to java LocalDateTime
    val nextDateTime = nowDateTime.toJavaLocalDateTime()
        .withHour(repeating.hour)
        .withMinute(repeating.minute)
        .withSecond(repeating.second)
        .toKotlinLocalDateTime()

    // Add any required offset
    val nextExecutionDateTime = when (repeating) {
        is Repeating.Daily -> {
            if (nextDateTime <= nowDateTime) {
                nextDateTime.toJavaLocalDateTime()
                    .plusDays(1)
                    .toKotlinLocalDateTime()
            } else {
                nextDateTime
            }
        }
        is Repeating.Weekly -> {
            if (nextDateTime <= nowDateTime) {
                nextDateTime.toJavaLocalDateTime()
                    .plusDays(7)
                    .toKotlinLocalDateTime()
            } else {
                nextDateTime
            }
        }
        is Repeating.Fortnightly -> {
            if (nextDateTime <= nowDateTime) {
                nextDateTime.toJavaLocalDateTime()
                    .plusDays(14)
                    .toKotlinLocalDateTime()
            } else {
                nextDateTime
            }
        }
    }
    return nextExecutionDateTime.toInstant(timeZone) - nowDateTime.toInstant(timeZone)
}
