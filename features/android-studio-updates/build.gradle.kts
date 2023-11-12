plugins {
    kotlin("jvm")
    id("io.gitlab.arturbosch.detekt")
}

group = "com.boswelja"
version = "1.0"

dependencies {
    implementation(projects.core.configuration)
    implementation(projects.core.scheduler)
    implementation(projects.core.logging)

    implementation(projects.core.network)
    implementation(libs.kotlinx.coroutines.core)

    implementation(libs.kord.core)
    implementation(libs.bundles.jackson.xml)

    testImplementation(kotlin("test"))
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockk)
    testImplementation(libs.turbine)
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

detekt {
    config.setFrom("$rootDir/config/detekt/detekt.yml")
    parallel = true
}
