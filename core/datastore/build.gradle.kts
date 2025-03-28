plugins {
    alias(libs.plugins.myKotlinMultiplatform)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(libs.datastore)
            api(libs.datastore.preferences)

            implementation(project(":core:common"))
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }

        androidMain.dependencies {

        }

        iosMain.dependencies {

        }

        desktopMain.dependencies {

        }
    }
}