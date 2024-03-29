plugins {
    kotlin("jvm")
    id("io.gitlab.arturbosch.detekt")
    id("java-test-fixtures")
}

group = "com.boswelja"
version = "1.0"

dependencies {
    api(libs.kotlinx.coroutines.core)
    testImplementation(kotlin("test"))
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

detekt {
    config.setFrom("$rootDir/config/detekt/detekt.yml")
    parallel = true
}
