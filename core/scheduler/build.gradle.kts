plugins {
    kotlin("jvm")
    id("io.gitlab.arturbosch.detekt")
}

group = "com.boswelja"
version = "1.0"

dependencies {
    api(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.datetime)

    testImplementation(kotlin("test"))
    testImplementation(libs.kotlinx.coroutines.test)
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

detekt {
    config.setFrom("$rootDir/config/detekt/detekt.yml")
    parallel = true
}
