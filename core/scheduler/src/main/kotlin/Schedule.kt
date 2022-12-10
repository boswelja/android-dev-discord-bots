import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toKotlinLocalDateTime
import kotlinx.datetime.toLocalDateTime
import java.time.temporal.ChronoField
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
    val nextExecutionDateTime = when (repeating) {
        is Repeating.Daily -> {
            nowDateTime.toJavaLocalDateTime()
                .plusDays(1)
                .withHour(repeating.hour)
                .withMinute(repeating.minute)
                .withMinute(repeating.second)
                .toKotlinLocalDateTime()
        }
        is Repeating.Weekly -> {
            nowDateTime.toJavaLocalDateTime()
                .plusWeeks(1)
                .with(ChronoField.DAY_OF_WEEK, repeating.weekday.value.toLong())
                .withHour(repeating.hour)
                .withMinute(repeating.minute)
                .withMinute(repeating.second)
                .toKotlinLocalDateTime()
        }
        is Repeating.Fortnightly -> {
            nowDateTime.toJavaLocalDateTime()
                .plusWeeks(2)
                .with(ChronoField.DAY_OF_WEEK, repeating.weekday.value.toLong())
                .withHour(repeating.hour)
                .withMinute(repeating.minute)
                .withMinute(repeating.second)
                .toKotlinLocalDateTime()
        }
    }
    return nextExecutionDateTime.toInstant(timeZone) - nowDateTime.toInstant(timeZone)
}
