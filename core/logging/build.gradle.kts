plugins {
    kotlin("jvm")
    id("io.gitlab.arturbosch.detekt")
}

group = "com.boswelja"
version = "1.0"

dependencies {
    testImplementation(kotlin("test"))
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

detekt {
    config.setFrom("$rootDir/config/detekt/detekt.yml")
    parallel = true
}
