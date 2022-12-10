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

suspend fun schedule(repeating: Repeating, block: suspend () -> Unit) {
    while (coroutineContext.isActive) {
        delay(calculateDelayUntilNextExecution(repeating))
        block()
    }
}

private fun calculateDelayUntilNextExecution(repeating: Repeating): Duration {
    val timeZone = TimeZone.currentSystemDefault()
    val nowDateTime = Clock.System.now().toLocalDateTime(timeZone)
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
