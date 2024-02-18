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
package feature

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

/**
 * A base class that orchestrates a list of [Feature]s.
 */
abstract class FeatureHost {

    protected val coroutineScope = CoroutineScope(SupervisorJob())

    /**
     * A list of [Feature]s that this host uses.
     */
    abstract val features: List<Feature>

    init {
        initFeatures()
    }

    /**
     * Called when an interaction should be registered.
     */
    abstract suspend fun registerInteraction(interaction: Interaction)

    protected fun initFeatures() {
        features.forEach { feature ->
            coroutineScope.launch {
                feature.interactions.forEach {
                    registerInteraction(it)
                }
                feature.init()
            }
        }
    }
}
