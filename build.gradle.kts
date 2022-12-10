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
        kotlin {
            target("**/*.kt")
            targetExclude("$buildDir/**/*.kt")
            targetExclude("bin/**/*.kt")

            ktlint("0.47.1")
                .setUseExperimental(true)
                .editorConfigOverride(
                    mapOf(
                        "ij_kotlin_allow_trailing_comma" to true,
                        "ij_kotlin_allow_trailing_comma_on_call_site" to true,
                    ),
                )
        }
    }
}
