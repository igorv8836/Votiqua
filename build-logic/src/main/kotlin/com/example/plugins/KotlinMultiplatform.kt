package com.example.plugins

import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.*

internal fun Project.configureKotlinMultiplatform(
    extension: KotlinMultiplatformExtension
) = extension.apply {
    jvmToolchain(17)

    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    jvm("desktop")

    listOf(
        iosArm64(),
        iosSimulatorArm64(),
        iosX64()
    )

    applyDefaultHierarchyTemplate()

    sourceSets.apply {
        commonMain {
            dependencies {
                implementation(libs.findLibrary("kotlinx.coroutines.core").get())
                implementation(libs.findLibrary("kotlinx-serialization-json").get())
                implementation(libs.findLibrary("napier").get())
                api(libs.findLibrary("koin.core").get())
            }
        }

        androidMain {
            dependencies {
                implementation(libs.findLibrary("koin.android").get())
                implementation(libs.findLibrary("kotlinx.coroutines.android").get())
            }
        }

        jvmMain.dependencies {
            implementation(libs.findLibrary("kotlinx.coroutines.swing").get())
        }
    }
}