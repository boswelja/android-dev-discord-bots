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
package dale

import dev.kord.core.Kord
import feature.Feature
import feature.FeatureHost
import feature.Interaction
import features.updates.androidstudio.AndroidStudioUpdateFeature
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import settings.SettingsDatabaseFactory

/**
 * A lifecycle-aware application that initializes and manages the Dale bot.
 */
class DaleBot(
    apiKey: String
) : FeatureHost() {

    private val kord = runBlocking { Kord(apiKey) }
    private val channelSettings = SettingsDatabaseFactory.channelSettingsInstance("dale")

    override suspend fun getFeatures(): List<Feature> = listOf(
        AndroidStudioUpdateFeature(kord, channelSettings)
    )

    override suspend fun registerInteraction(interaction: Interaction) {
        TODO("Not yet implemented")
    }

    override fun onFeaturesRegistered() {
        lifecycleScope.launch {
            kord.login()
        }
    }
}