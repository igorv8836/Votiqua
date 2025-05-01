plugins {
    alias(libs.plugins.myKotlinMultiplatform)
    alias(libs.plugins.myComposeMultiplatform)
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.components.resources)
            }
        }
    }
}

compose.resources {
    publicResClass = true
    generateResClass = always
}