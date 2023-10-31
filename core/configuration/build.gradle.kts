plugins {
    kotlin("jvm")
    id("app.cash.sqldelight")
    id("io.gitlab.arturbosch.detekt")
}

group = "com.boswelja"
version = "1.0"

dependencies {
    api(libs.kotlinx.coroutines.core)
    implementation(libs.bundles.sqldelight)
    testImplementation(kotlin("test"))
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

sqldelight {
    databases {
        create("Settings") {
            packageName.set("settings.database")
        }
    }
}

detekt {
    config.setFrom("$rootDir/config/detekt/detekt.yml")
    parallel = true
}
