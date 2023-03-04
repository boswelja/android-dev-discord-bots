plugins {
    kotlin("jvm")
    application
    id("io.gitlab.arturbosch.detekt")
}

group = "com.boswelja"
version = "1.0-SNAPSHOT"

dependencies {
    implementation(projects.core.configuration)
    implementation(projects.core.discord)
    implementation(projects.core.scheduler)

    implementation(libs.apptastic.rssreader)

    testImplementation(kotlin("test"))
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

application {
    mainClass.set("MainKt")
}

detekt {
    config.setFrom("$rootDir/config/detekt/detekt.yml")
    parallel = true
}
