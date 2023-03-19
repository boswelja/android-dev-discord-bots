package features.updates.library.updatesource

import kotlinx.datetime.TimeZone

data class MavenPom(
    val groupId: String,
    val artifactId: String,
    val version: String,
    val packaging: String,
    val name: String,
    val description: String,
    val homepageUrl: String,
    val inceptionYear: String,
    val organization: Organization,
    val licenses: List<License>,
    val developers: List<Developer>,
    val contributors: List<Contributor>,
    val scm: Scm,
    val issueManagement: IssueManagement,
    val ciManagement: CiManagement,
) {

    data class Organization(
        val name: String,
        val url: String,
    )

    data class License(
        val name: String,
        val url: String,
        val distribution: Distribution,
        val comments: String,
    ) {
        enum class Distribution {
            Repo,
            Manual,
        }
    }

    data class Developer(
        val id: String,
        val name: String,
        val email: String,
        val homepageUrl: String,
        val organizationName: String,
        val organizationUrl: String,
        val roles: List<String>,
        val timezone: TimeZone,
    )

    data class Contributor(
        val name: String,
        val email: String,
        val homepageUrl: String,
        val organizationName: String,
        val organizationUrl: String,
        val roles: List<String>,
        val timezone: TimeZone,
    )

    data class Scm(
        val connection: String,
        val developerConnection: String,
        val tag: String,
        val url: String,
    )

    data class IssueManagement(
        val systemName: String,
        val url: String,
    )

    data class CiManagement(
        val systemName: String,
        val url: String,
    )
}
