/*
 * Copyright 2023 AndroidDev Discord Dev Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package scheduler

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
        override val second: Int = 0,
    ) : Repeating {
        override val interval: Duration = 1.days
    }

    /**
     * Configuration for weekly repeating work.
     * @property weekday The day of the week this work should run on.
     * @property hour The hour of the day this work should run on.
     * @property minute The minute of the day this work should run on.
     * @property second The second of the day this work should run on.
     */
    data class Weekly(
        val weekday: DayOfWeek,
        override val hour: Int = 0,
        override val minute: Int = 0,
        override val second: Int = 0,
    ) : Repeating {
        override val interval: Duration = 7.days
    }

    /**
     * Configuration for fortnightly repeating work.
     * @property weekday The day of the week this work should run on.
     * @property hour The hour of the day this work should run on.
     * @property minute The minute of the day this work should run on.
     * @property second The second of the day this work should run on.
     */
    data class Fortnightly(
        val weekday: DayOfWeek,
        override val hour: Int = 0,
        override val minute: Int = 0,
        override val second: Int = 0,
    ) : Repeating {
        override val interval: Duration = 14.days
    }
}
