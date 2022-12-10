import java.time.DayOfWeek

sealed class Repeating {
    abstract val hour: Int
    abstract val minute: Int
    abstract val second: Int

    data class Daily(
        override val hour: Int = 0,
        override val minute: Int = 0,
        override val second: Int = 0
    ) : Repeating()

    data class Weekly(
        val weekday: DayOfWeek,
        override val hour: Int = 0,
        override val minute: Int = 0,
        override val second: Int = 0
    ) : Repeating()

    data class Fortnightly(
        val weekday: DayOfWeek,
        override val hour: Int = 0,
        override val minute: Int = 0,
        override val second: Int = 0
    ) : Repeating()
}
