plugins {
    kotlin("jvm")
    id("com.squareup.sqldelight")
    id("io.gitlab.arturbosch.detekt")
}

group = "com.boswelja"
version = "1.0"

dependencies {
    implementation(projects.core.configuration)
    implementation(projects.core.discord)
    implementation(projects.core.scheduler)
    implementation(projects.core.logging)
    implementation(projects.core.network)

    implementation(libs.bundles.sqldelight)

    implementation("org.apache.maven:maven-repository-metadata:3.9.1")
    implementation("org.apache.maven:maven-model:3.9.1")

    testImplementation(kotlin("test"))
}

sqldelight {
    database("MavenIndex") {
        packageName = "features.updates.library.database"
    }
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

detekt {
    config.setFrom("$rootDir/config/detekt/detekt.yml")
    parallel = true
}
