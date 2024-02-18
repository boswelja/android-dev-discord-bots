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
package features.updates.library.versionsource

import features.updates.library.database.MavenVersionQueries
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.TimeZone
import network.NetworkModule
import org.apache.maven.artifact.repository.metadata.io.xpp3.MetadataXpp3Reader
import org.apache.maven.model.io.xpp3.MavenXpp3Reader

class MavenArtifactVersionSourceImpl(
    private val network: NetworkModule,
    private val versionQueries: MavenVersionQueries,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : MavenArtifactVersionSource {

    private val pomReader = MavenXpp3Reader()
    private val metadataReader = MetadataXpp3Reader()

    override suspend fun getLatestVersions(coordinateUrl: String): MavenVersions {
        val url = "$coordinateUrl/maven-metadata.xml"
        val pomText = network.downloadFileAsText(url)
        val metadata = metadataReader.read(pomText.reader())
        return MavenVersions(
            latest = metadata.versioning.latest,
            release = metadata.versioning.release,
            allVersions = metadata.versioning.versions,
        )
    }

    override suspend fun getPom(artifactUrl: String): MavenPom {
        val pomText = network.downloadFileAsText(artifactUrl)
        val model = pomReader.read(pomText.reader())
        return MavenPom(
            groupId = model.groupId,
            artifactId = model.artifactId,
            version = model.version,
            packaging = model.packaging,
            name = model.name,
            description = model.description,
            homepageUrl = model.url,
            inceptionYear = model.inceptionYear,
            organization = MavenPom.Organization(
                name = model.organization.name,
                url = model.organization.url,
            ),
            licenses = model.licenses.map {
                MavenPom.License(
                    name = it.name,
                    url = it.url,
                    distribution = when (it.distribution) {
                        "repo" -> MavenPom.License.Distribution.Repo
                        "manual" -> MavenPom.License.Distribution.Manual
                        else -> error("Unknown distribution type ${it.distribution}")
                    },
                    comments = it.comments,
                )
            },
            developers = model.developers.map {
                MavenPom.Developer(
                    id = it.id,
                    name = it.name,
                    email = it.email,
                    homepageUrl = it.url,
                    organizationName = it.organization,
                    organizationUrl = it.organizationUrl,
                    roles = it.roles,
                    timezone = TimeZone.of(it.timezone),
                )
            },
            contributors = model.contributors.map {
                MavenPom.Contributor(
                    name = it.name,
                    email = it.email,
                    homepageUrl = it.url,
                    organizationName = it.organization,
                    organizationUrl = it.organizationUrl,
                    roles = it.roles,
                    timezone = TimeZone.of(it.timezone),
                )
            },
            scm = MavenPom.Scm(
                connection = model.scm.connection,
                developerConnection = model.scm.developerConnection,
                url = model.scm.url,
                tag = model.scm.tag,
            ),
            issueManagement = MavenPom.IssueManagement(
                systemName = model.issueManagement.system,
                url = model.issueManagement.url,
            ),
            ciManagement = MavenPom.CiManagement(
                systemName = model.ciManagement.system,
                url = model.ciManagement.url,
            ),
        )
    }

    override suspend fun getLastKnownVersion(artifactId: String): String? {
        return withContext(dispatcher) {
            versionQueries.getVersion(artifactId).executeAsOneOrNull()
        }
    }

    override suspend fun updateLastKnownVersion(artifactId: String, version: String) {
        withContext(dispatcher) {
            versionQueries.storeNewVersion(artifactId, version)
        }
    }
}
