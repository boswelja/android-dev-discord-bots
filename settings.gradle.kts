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
    "dale",
)
