rootProject.name = "Votiqua"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
    }
}

include(":composeApp")
findProject(":composeApp")?.name = "mainApp"
include(":server:main")
findProject(":server:main")?.name = "mainServer"

include(":shared")

include(":orbit_mvi")
include(":core:common")
include(":core:network")
include(":core:datastore")

include("server:common")
include(":server:feature:auth-api")
include(":server:feature:auth")
include(":server:feature:voting")
include(":server:feature:profile")
include(":server:feature:search")
