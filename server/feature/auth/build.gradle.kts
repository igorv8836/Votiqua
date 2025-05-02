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
    implementation(projects.shared)
    implementation(projects.server.feature.authApi)
    implementation(projects.server.feature.profileApi)

    implementation(libs.bundles.server.database)
    implementation(libs.bundles.ktor.server.core)
    implementation(libs.email)
}

tasks.test {
    useJUnitPlatform()
}