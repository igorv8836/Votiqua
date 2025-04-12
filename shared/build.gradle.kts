plugins {
    alias(libs.plugins.myKotlinMultiplatform)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kotlinx.datetime)
        }
    }
}