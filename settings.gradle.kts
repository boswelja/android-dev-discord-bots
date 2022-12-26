rootProject.name = "android-dev-discord-bot"

dependencyResolutionManagement {
    repositories {
        mavenCentral()
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
