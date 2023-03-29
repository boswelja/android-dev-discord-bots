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
package features.updates.library

import features.Feature
import features.updates.library.mavenindexer.MavenIndexerImpl
import features.updates.library.versionsource.MavenArtifactVersionSource
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import scheduler.scheduleRepeating
import kotlin.time.Duration.Companion.days

class LibraryUpdateFeature(
    private val indexer: MavenIndexerImpl,
    private val updateSource: MavenArtifactVersionSource
) : Feature {
    override suspend fun init(): Unit = supervisorScope {
        launch {
            scheduleRepeating(1.days) {
                indexer.updateIndex()
            }
        }
    }
}
