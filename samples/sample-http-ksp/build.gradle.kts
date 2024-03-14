plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.ksp)
}

kotlin {

    jvm("desktop")

    sourceSets {
        commonMain.dependencies {
            api(projects.http.httpCore)

        }
    }
}

dependencies {
    add("kspDesktop", projects.http.httpKsp)
}
