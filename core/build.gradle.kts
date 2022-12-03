plugins {
    kotlin("jvm")
}

group = "com.boswelja"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    api("dev.kord:kord-core:0.8.0-M17")

    testImplementation(kotlin("test"))
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}
