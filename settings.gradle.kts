rootProject.name = "android-dev-discord-bot"

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

include(
    "core:database",
    "core:network",
    "core:rss",
    "core:discord",
    "dale",
)
