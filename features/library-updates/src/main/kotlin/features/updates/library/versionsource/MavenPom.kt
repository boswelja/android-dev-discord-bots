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
