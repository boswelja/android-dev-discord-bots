import java.time.DayOfWeek
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days

sealed interface Repeating {
    val hour: Int
    val minute: Int
    val second: Int

    val interval: Duration

    data class Daily(
        override val hour: Int = 0,
        override val minute: Int = 0,
        override val second: Int = 0
    ) : Repeating {
        override val interval: Duration = 1.days
    }

    data class Weekly(
        val weekday: DayOfWeek,
        override val hour: Int = 0,
        override val minute: Int = 0,
        override val second: Int = 0
    ) : Repeating {
        override val interval: Duration = 7.days
    }

    data class Fortnightly(
        val weekday: DayOfWeek,
        override val hour: Int = 0,
        override val minute: Int = 0,
        override val second: Int = 0
    ) : Repeating {
        override val interval: Duration = 14.days
    }
}
