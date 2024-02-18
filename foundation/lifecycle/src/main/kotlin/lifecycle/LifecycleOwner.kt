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

/**
 * An implementation of a "lifecycle owner". A lifecycle owner manages the lifecycle of its children alongside its own
 * lifecycle.
 */
abstract class LifecycleOwner : Lifecycle() {
    private val childLifecycles = mutableSetOf<Lifecycle>()

    override fun onDestroy() {
        super.onDestroy()
        childLifecycles.removeAll {
            it.destroy()
            true
        }
    }

    /**
     * Registers a new [Lifecycle] under this lifecycle owner. The given lifecycle will be created if added
     * successfully. This is a no-op if the lifecycle was already added.
     */
    fun registerLifecycle(lifecycle: Lifecycle) {
        check(lifecycleState == LifecycleState.CREATED) {
            "Lifecycle state must be CREATED to manage children, but the state of this owner is $lifecycleState"
        }
        if (childLifecycles.add(lifecycle)) {
            lifecycle.create()
        }
    }

    /**
     * Unregisters an existing [Lifecycle] under this lifecycle owner. The given lifecycle will be destroyed if removed
     * successfully. This is a no-op if the lifecycle was already removed.
     */
    fun unregisterLifecycle(lifecycle: Lifecycle) {
        check(lifecycleState == LifecycleState.CREATED) {
            "Lifecycle state must be CREATED to manage children, but the state of this owner is $lifecycleState"
        }
        if (childLifecycles.remove(lifecycle)) {
            lifecycle.destroy()
        }
    }
}
