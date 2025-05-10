plugins {
    alias(libs.plugins.myKotlinMultiplatform)
    alias(libs.plugins.myComposeMultiplatform)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.common)
            implementation(projects.core.uiCommon)
            implementation(projects.core.network)
            implementation(projects.core.datastore)
            implementation(projects.shared)
            implementation(projects.orbitMvi)
            implementation(projects.feature.votingApi)

            implementation(libs.ktor.client.core)
            implementation(libs.kotlinx.datetime)
            implementation("com.github.skydoves:landscapist-coil3:2.4.7")

            implementation(libs.coil.compose)
            implementation(libs.coil.compose.core)
            implementation(libs.coil.network.ktor)
            implementation(libs.coil.mp)
        }
    }
}
