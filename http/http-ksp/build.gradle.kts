plugins {
    alias(libs.plugins.kotlinJvm)

    `maven-publish`
}

dependencies {
    api(projects.http.httpCore)
    api(projects.kspExt)

    api(libs.ksp)
}

