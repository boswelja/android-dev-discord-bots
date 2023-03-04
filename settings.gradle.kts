rootProject.name = "android-dev-discord-bot"

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        maven("https://oss.sonatype.org/content/repositories/snapshots")
    }
}

include(
    "core:configuration",
    "core:discord",
    "core:fetcher",
    ":core:network",
    "core:scheduler",
    "dale",
)

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
