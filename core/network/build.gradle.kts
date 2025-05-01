plugins {
    alias(libs.plugins.myKotlinMultiplatform)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.logging)
            implementation(libs.ktor.client.websockets)
            implementation(libs.ktor.client.auth)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.client.serialization)
            implementation(libs.ktor.serialization.kotlinx.json)

            implementation(projects.core.common)
            implementation(projects.shared)
            implementation(projects.core.datastore)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }

        androidMain.dependencies {
            implementation(libs.ktor.client.engine.okhttp)
        }

        iosMain.dependencies {
            implementation(libs.ktor.client.engine.darwin)
        }

        desktopMain.dependencies {
            implementation(libs.ktor.client.engine.java)
        }
    }
}