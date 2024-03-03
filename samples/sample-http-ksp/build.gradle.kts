plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ksp)
}

dependencies {
    api(projects.http.httpCore)

    ksp(projects.http.httpKsp)
}