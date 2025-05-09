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
            implementation(projects.feature.recomApi)
            implementation(projects.feature.votingApi)

            implementation(libs.ktor.client.core)
            implementation(libs.kotlinx.datetime)
        }
    }
}