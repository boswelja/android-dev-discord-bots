plugins {
    kotlin("jvm")
    application
}

group = "com.boswelja"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":core:database"))
    api(project(":core:rss")) // FIXME do not expose RSS

    testImplementation(kotlin("test"))
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

application {
    mainClass.set("MainKt")
}
