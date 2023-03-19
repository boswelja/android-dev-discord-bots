package features.updates.library.updatesource

interface MavenArtifactUpdateSource {

    suspend fun getVersionsFor(groupId: String, artifactId: String): MavenVersions

    suspend fun getPomFor(groupId: String, artifactId: String, version: String): MavenPom
}
