package features.updates.library.updatesource

import kotlinx.datetime.TimeZone
import network.NetworkModule
import org.apache.maven.artifact.repository.metadata.io.xpp3.MetadataXpp3Reader
import org.apache.maven.model.io.xpp3.MavenXpp3Reader

class MavenCentralArtifactUpdateSource(
    private val network: NetworkModule
) : MavenArtifactUpdateSource {

    private val pomReader = MavenXpp3Reader()
    private val metadataReader = MetadataXpp3Reader()

    override suspend fun getVersionsFor(groupId: String, artifactId: String): MavenVersions {
        val url = "https://repo.maven.apache.org/maven2/${groupId.replace(".", "/")}/${artifactId}/maven-metadata.xml"
        val pomText = network.downloadFileAsText(url)
        val metadata = metadataReader.read(pomText.reader())
        return MavenVersions(
            latest = metadata.versioning.latest,
            release = metadata.versioning.release,
            allVersions = metadata.versioning.versions
        )
    }

    override suspend fun getPomFor(groupId: String, artifactId: String, version: String): MavenPom {
        val url = "https://repo.maven.apache.org/maven2/${groupId.replace(".", "/")}/${artifactId}/${version}/${artifactId}-${version}.pom"
        val pomText = network.downloadFileAsText(url)
        val model = pomReader.read(pomText.reader())
        return MavenPom(
            groupId = groupId,
            artifactId = artifactId,
            version = version,
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
            )
        )
    }
}
