/*
 * Copyright 2024 AndroidDev Discord Dev Team
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
package lifecycle

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.ensureActive

/**
 * An implementation of a "Lifecycle".
 */
abstract class Lifecycle {

    /**
     * The current state of the Lifecycle. See [LifecycleState] for possible states.
     */
    var lifecycleState: LifecycleState = LifecycleState.NOT_YET_CREATED
        private set

    protected val lifecycleScope: CoroutineScope = CoroutineScope(SupervisorJob())

    init {
        create()
    }

    internal fun create() {
        check(lifecycleState < LifecycleState.CREATED) {
            "Tried to create lifecycle, but lifecycle was in state $lifecycleState"
        }
        lifecycleState = LifecycleState.CREATED
        lifecycleScope.ensureActive()
        onCreate()
    }

    internal fun destroy() {
        check(lifecycleState < LifecycleState.DESTROYED) {
            "Tried to destroy lifecycle, but lifecycle was in state $lifecycleState"
        }
        lifecycleState = LifecycleState.DESTROYED
        onDestroy()
        lifecycleScope.cancel(message = "The parent Lifecycle was destroyed")
    }

    protected open fun onCreate() { }

    protected open fun onDestroy() { }
}

/**
 * Describes the state of a [Lifecycle].
 */
@JvmInline
value class LifecycleState internal constructor(internal val value: Int) : Comparable<LifecycleState> {

    override fun compareTo(other: LifecycleState): Int = this.value.compareTo(other.value)

    companion object {
        /**
         * The lifecycle state is "not yet created", meaning it is not yet safe to use lifecycle-related functions
         * within the object.
         */
        val NOT_YET_CREATED = LifecycleState(0)

        /**
         * The lifecycle state is "created", meaning the lifecycle exists and can be fully utilised.
         */
        val CREATED = LifecycleState(1)

        /**
         * The lifecycle state is "destroyed", which means any work has been cancelled and the object will be disposed
         * of ASAP.
         */
        val DESTROYED = LifecycleState(2)
    }
}
