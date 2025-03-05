plugins {
    alias(libs.plugins.myKotlinMultiplatform)
    alias(libs.plugins.myComposeMultiplatform)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.navigation.compose)

            implementation(libs.napier)


            implementation(libs.koin.core)
            implementation(libs.koin.compose)

            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.kotlinx.coroutines.core)


            implementation(project(":orbit_mvi"))
        }
    }
}