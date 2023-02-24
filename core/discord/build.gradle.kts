plugins {
    kotlin("jvm")
    id("io.gitlab.arturbosch.detekt")
}

group = "com.boswelja"
version = "1.0-SNAPSHOT"

dependencies {
    api(libs.kotlinx.coroutines.core)
    implementation(libs.bundles.kord)
    implementation(libs.jda) {
        exclude(module = "opus-java") // We don't need audio components (yet)
    }

    testImplementation(kotlin("test"))
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

detekt {
    config.setFrom("$rootDir/config/detekt/detekt.yml")
    parallel = true
}
