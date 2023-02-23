rootProject.name = "android-dev-discord-bot"

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        maven("https://oss.sonatype.org/content/repositories/snapshots")
    }
}

include(
    "common:network",
    "core:configuration",
    "core:fetcher",
    "core:discord",
    "core:scheduler",
    "dale",
)

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
