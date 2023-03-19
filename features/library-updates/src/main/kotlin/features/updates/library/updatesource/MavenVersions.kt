package features.updates.library.updatesource

data class MavenVersions(
    val latest: String,
    val release: String,
    val allVersions: List<String>,
)
