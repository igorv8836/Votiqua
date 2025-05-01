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
        }
    }
}
