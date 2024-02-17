rootProject.name = "android-dev-discord-bot"

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        maven("https://oss.sonatype.org/content/repositories/snapshots")
    }
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
}

include(
    "core:configuration",
    "core:feature",
    "core:network",
    "core:scheduler",
    "features:android-studio-updates",
    "foundation:lifecycle",
    "foundation:logging",
    "dale",
)

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
