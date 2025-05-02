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
findProject(":composeApp")?.name = "main-app"
include(":server:main")
findProject(":server:main")?.name = "main-server"

include(":shared")

include(":orbit_mvi")
include(":core:common")
include(":core:ui-common")
include(":core:network")
include(":core:datastore")

include(":feature:auth-api")
include(":feature:auth")
findProject(":feature:auth")?.name = "auth-impl"
include(":feature:profile-api")
include(":feature:profile")
findProject(":feature:profile")?.name = "profile-impl"
include(":feature:voting-api")
include(":feature:voting")
findProject(":feature:voting")?.name = "voting-impl"
include(":feature:recom-api")
include(":feature:recom")
findProject(":feature:recom")?.name = "recom-impl"

include("server:common")
include(":server:feature:auth-api")
include(":server:feature:auth")
findProject(":server:feature:auth")?.name = "auth-impl"
include(":server:feature:voting-api")
include(":server:feature:voting")
findProject(":server:feature:voting")?.name = "voting-impl"
include(":server:feature:profile-api")
include(":server:feature:profile")
findProject(":server:feature:profile")?.name = "profile-impl"
include(":server:feature:recom")