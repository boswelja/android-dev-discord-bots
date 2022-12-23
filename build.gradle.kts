plugins {
    kotlin("jvm") version "1.7.22" apply false
    id("com.squareup.sqldelight") version "1.5.4" apply false
    id("io.gitlab.arturbosch.detekt") version "1.22.0" apply false
    id("com.diffplug.spotless") version "6.12.0"
}

buildscript {
    dependencies {
        classpath("com.pinterest:ktlint:0.47.1")
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
            targetExclude("$buildDir/**/*.kt")
            targetExclude("bin/**/*.kt")

            ktlint()
                .setUseExperimental(true)
                // spotless doesn't yet read from .editorconfig
                // see https://github.com/diffplug/spotless/issues/142
                .editorConfigOverride(
                    mapOf(
                        "ij_kotlin_allow_trailing_comma" to true,
                        "ij_kotlin_allow_trailing_comma_on_call_site" to true,
                    ),
                )
            trimTrailingWhitespace()
            endWithNewline()
            licenseHeaderFile(rootProject.file("spotless/copyright.kt"))
        }
    }
}
