plugins {
    kotlin("jvm")
    application
    id("io.gitlab.arturbosch.detekt")
}

group = "com.boswelja"
version = "1.0"

dependencies {
    implementation(projects.core.configuration)
    implementation(projects.foundation.logging)
    implementation(projects.core.feature)
    implementation(projects.foundation.lifecycle)

    implementation(libs.kord.core)
    implementation(projects.features.androidStudioUpdates)

    testImplementation(kotlin("test"))
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

application {
    mainClass.set("dale.MainKt")
}

detekt {
    config.setFrom("$rootDir/config/detekt/detekt.yml")
    parallel = true
}
