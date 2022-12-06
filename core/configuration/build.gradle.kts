plugins {
    kotlin("jvm")
    id("com.squareup.sqldelight")
    id("io.gitlab.arturbosch.detekt")
}

group = "com.boswelja"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")

    implementation("org.xerial:sqlite-jdbc:3.40.0.0")
    implementation("com.squareup.sqldelight:sqlite-driver:1.5.4")
    implementation("com.squareup.sqldelight:coroutines-extensions-jvm:1.5.4")

    testImplementation(kotlin("test"))
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

sqldelight {
    database("GuildSettings") {
        packageName = "guildsettings.database"
    }
}

detekt {
    config.setFrom("$rootDir/config/detekt/detekt.yml")
    parallel = true
}
