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
package features.updates.library.mavenindexer

import features.updates.library.database.MavenIndexQueries
import features.updates.library.mavenindexer.indexsource.MavenCoordinate
import features.updates.library.mavenindexer.indexsource.MavenIndexSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext

class MavenIndexerImpl(
    private val indexDatabase: MavenIndexQueries,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : MavenIndexer {

    private val indexers: List<MavenIndexSource> = emptyList()

    override suspend fun updateIndex() = supervisorScope {
        withContext(dispatcher) {
            // Collect coordinates from indexers
            val coordinates = indexers.map {
                async {
                    it.getAvailableArtifacts()
                }
            }.awaitAll().flatten()

            // Update the database
            indexDatabase.transaction {
                indexDatabase.deleteAll()
                coordinates.forEach {
                    indexDatabase.insert(it.groupId, it.artifactId, it.url)
                }
            }
        }
    }

    override suspend fun getArtifactsMatching(groupId: Regex): List<MavenCoordinate> {
        return withContext(dispatcher) {
            indexDatabase.getMatching(groupId.pattern) { groupId, artifactId, coordinateUrl ->
                MavenCoordinate(groupId, artifactId, coordinateUrl)
            }.executeAsList()
        }
    }
}
