plugins {
    alias(libs.plugins.myKotlinMultiplatform)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.ktor.core)
            implementation(libs.ktor.logging)
            implementation(libs.ktor.websockets)
            implementation(libs.ktor.auth)
            implementation(libs.ktor.content.negotiation)
            implementation(libs.ktor.serialization)
            implementation(libs.ktor.serialization.kotlinx.json)

            implementation(project(":core:common"))
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }

        androidMain.dependencies {
            implementation(libs.ktor.engine.okhttp)
        }

        iosMain.dependencies {
            implementation(libs.ktor.engine.darwin)
        }

        desktopMain.dependencies {
            implementation(libs.ktor.engine.java)
        }
    }
}