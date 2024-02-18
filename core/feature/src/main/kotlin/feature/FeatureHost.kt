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

import kotlinx.coroutines.launch
import lifecycle.LifecycleOwner
import logging.logInfo

/**
 * An extension of [LifecycleOwner] that manages [Feature]s.
 */
abstract class FeatureHost : LifecycleOwner() {

    /**
     * Gets the list of features that this feature host will manage. This will only ever be called once.
     */
    abstract suspend fun getFeatures(): List<Feature>

    /**
     * Called when the implementer should register an interaction for a feature.
     */
    abstract suspend fun registerInteraction(interaction: Interaction)

    /**
     * Called when all features have been registered.
     */
    open fun onFeaturesRegistered() {}

    override fun onCreate() {
        super.onCreate()
        lifecycleScope.launch {
            val features = getFeatures()
            features.forEach { feature ->
                logInfo { "Initializing feature $feature" }
                registerLifecycle(feature)
                when (feature) {
                    is TextBasedFeature -> {
                        feature.interactions.forEach { interaction ->
                            logInfo { "Registering interaction $interaction" }
                            registerInteraction(interaction)
                        }
                    }
                }
                onFeaturesRegistered()
            }
        }
    }
}
