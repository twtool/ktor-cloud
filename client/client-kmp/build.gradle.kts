plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)

    `maven-publish`
}

kotlin {
    jvm("desktop")

    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "11"
            }
        }

        publishLibraryVariants("release")
    }

    sourceSets {
        val desktopMain by getting

        commonMain.dependencies {
            api(projects.http.httpCore)
            api(libs.ktor.client.content.negotiation)
            api(libs.ktor.serialization.kotlinx.json)
        }

        androidMain.dependencies {
            api(libs.ktor.client.okhttp)
        }

        desktopMain.dependencies {
            api(libs.ktor.client.java)
        }
    }
}

android {
    namespace = "icu.twtool.ktor.cloud.client.kmp"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}