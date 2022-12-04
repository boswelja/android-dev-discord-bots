rootProject.name = "android-dev-discord-bot"

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

include(
    "common:network",
    "core:database",
    "core:fetcher",
    "core:discord",
    "dale",
)
