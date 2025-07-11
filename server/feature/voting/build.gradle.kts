plugins {
    alias(libs.plugins.jetbrains.kotlin.jvm)
    alias(libs.plugins.ktor)
    alias(libs.plugins.kotlin.serialization)
}

application {
    mainClass.set("org.example.votiqua.ApplicationKt")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=${extra["io.ktor.development"] ?: "false"}")
}

dependencies {
    implementation(projects.server.common)
    implementation(projects.server.feature.authApi)
    implementation(projects.server.feature.votingApi)
    implementation(projects.server.feature.profileApi)
    implementation(projects.shared)

    implementation(libs.bundles.server.database)
    implementation(libs.bundles.ktor.server.core)
    implementation(libs.kotlinx.datetime)
}

tasks.test {
    useJUnitPlatform()
}