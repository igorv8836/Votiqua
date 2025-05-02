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
            implementation(projects.orbitMvi)
            implementation(projects.shared)

            implementation(projects.feature.profileApi)
            implementation(projects.feature.votingApi)
            implementation(projects.feature.authApi)


            implementation(libs.coil.compose)
            implementation(libs.coil.compose.core)
            implementation(libs.coil.network.ktor)
            implementation(libs.coil.mp)
            implementation(libs.ktor.client.core)
            implementation("com.github.skydoves:landscapist-coil3:2.4.7")
        }

        androidMain.dependencies {
            implementation(libs.ktor.client.engine.okhttp)
        }

        desktopMain.dependencies {
            implementation(libs.ktor.client.engine.java)
//            implementation("io.coil-kt.coil3:coil-skiko:0.9.4")
        }

        iosMain.dependencies {
            implementation(libs.ktor.client.engine.darwin)
        }
    }
}
