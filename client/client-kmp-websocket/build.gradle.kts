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
            api(projects.client.clientKmp)
            api(libs.ktor.client.websockets)
        }

        androidMain.dependencies {
        }

        desktopMain.dependencies {
        }
    }
}

android {
    namespace = "icu.twtool.ktor.cloud.client.kmp.websocket"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}