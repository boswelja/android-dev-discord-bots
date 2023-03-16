plugins {
    kotlin("jvm")
    id("io.gitlab.arturbosch.detekt")
}

group = "com.boswelja"
version = "1.0"

dependencies {
    implementation(projects.core.configuration)
    implementation(projects.core.fetcher)
    implementation(projects.core.discord)
    implementation(projects.core.scheduler)
    implementation(projects.core.logging)

    testImplementation(kotlin("test"))
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

detekt {
    config.setFrom("$rootDir/config/detekt/detekt.yml")
    parallel = true
}
