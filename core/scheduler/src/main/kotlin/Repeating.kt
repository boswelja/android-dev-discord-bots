import java.time.DayOfWeek
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days

/**
 * Holds configuration for repeating work, scheduled by [schedule].
 */
sealed interface Repeating {

    /**
     * The hour of the day this work should run on.
     */
    val hour: Int

    /**
     * The minute of the day this work should run on.
     */
    val minute: Int

    /**
     * The second of the day this work should run on.
     */
    val second: Int

    /**
     * The interval between work runs, excluding the time taken for the work itself to run.
     */
    val interval: Duration

    /**
     * Configuration for daily repeating work.
     */
    data class Daily(
        override val hour: Int = 0,
        override val minute: Int = 0,
        override val second: Int = 0
    ) : Repeating {
        override val interval: Duration = 1.days
    }

    /**
     * Configuration for weekly repeating work.
     * @property weekday The day of the week this work should run on.
     */
    data class Weekly(
        val weekday: DayOfWeek,
        override val hour: Int = 0,
        override val minute: Int = 0,
        override val second: Int = 0
    ) : Repeating {
        override val interval: Duration = 7.days
    }

    /**
     * Configuration for fortnightly repeating work.
     * @property weekday The day of the week this work should run on.
     */
    data class Fortnightly(
        val weekday: DayOfWeek,
        override val hour: Int = 0,
        override val minute: Int = 0,
        override val second: Int = 0
    ) : Repeating {
        override val interval: Duration = 14.days
    }
}
