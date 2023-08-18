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
        create("GuildSettings") {
            packageName.set("guildsettings.database")
        }
    }
}

detekt {
    config.setFrom("$rootDir/config/detekt/detekt.yml")
    parallel = true
}
