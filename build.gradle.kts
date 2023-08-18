plugins {
    kotlin("jvm") version "1.9.0" apply false
    id("app.cash.sqldelight") version "2.0.0" apply false
    id("io.gitlab.arturbosch.detekt") version "1.23.1" apply false
    id("com.diffplug.spotless") version "6.20.0"
}

buildscript {
    dependencies {
        classpath("com.pinterest:ktlint:0.50.0")
    }
}

subprojects {
    apply(plugin = "com.diffplug.spotless")

    spotless {
        encoding = Charsets.UTF_8
        format("gradlekts") {
            target("**/*.gradle.kts")

            trimTrailingWhitespace()
            endWithNewline()
        }
        kotlin {
            target("**/*.kt")
            targetExclude("${layout.buildDirectory}/**/*.kt")
            targetExclude("bin/**/*.kt")

            ktlint()
            trimTrailingWhitespace()
            endWithNewline()
            licenseHeaderFile(rootProject.file("spotless/copyright.kt"))
        }
    }
}
