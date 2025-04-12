plugins {
    alias(libs.plugins.jetbrains.kotlin.jvm)
    alias(libs.plugins.ktor)
    alias(libs.plugins.kotlin.serialization)
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

kotlin {
    jvmToolchain {
        this.languageVersion.set(JavaLanguageVersion.of(21))
    }
}

ktor{
    fatJar{
        archiveFileName.set("votiqua.jar")
    }
}

group = "org.example.votiqua"
version = "1.0.0"
application {
    mainClass.set("org.example.votiqua.ApplicationKt")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=${extra["io.ktor.development"] ?: "false"}")
}

dependencies {
    implementation(projects.server.common)
    implementation(projects.shared)
    implementation(projects.server.feature.authApi)
    implementation(projects.server.feature.auth)
    implementation(projects.server.feature.search)
    implementation(projects.server.feature.profile)
    implementation(projects.server.feature.voting)

    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.datetime)

    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.auth)
    implementation(libs.ktor.server.auth.jwt)
    implementation(libs.ktor.server.auto.head.response)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.server.logging)
    implementation(libs.ktor.server.status.pages)
    implementation(libs.ktor.server.metrics.micrometer)
    implementation(libs.ktor.server.forwarded.header)
    implementation(libs.ktor.server.cors)

    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.apache)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.client.content.negotiation)

    implementation(libs.logback.classic)

    implementation(libs.koin.core)
    implementation(libs.koin.ktor)

    implementation(libs.exposed.core)
    implementation(libs.exposed.jdbc)
    implementation(libs.exposed.java.time)
    implementation(libs.exposed.dao)
    implementation(libs.postgresql)
    implementation(libs.hikari)

    implementation(libs.kotlin.faker)
}