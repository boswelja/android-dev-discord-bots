plugins {
    kotlin("jvm")
    id("com.squareup.sqldelight")
}

group = "com.boswelja"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    api("dev.kord:kord-core:0.8.0-M17")

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
