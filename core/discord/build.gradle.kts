plugins {
    kotlin("jvm")
}

group = "com.boswelja"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation("dev.kord:kord-core:0.8.0-M17")

    testImplementation(kotlin("test"))
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}