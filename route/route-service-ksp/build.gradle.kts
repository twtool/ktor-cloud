plugins {
    alias(libs.plugins.kotlinJvm)

    `maven-publish`
}

dependencies {
    api(projects.http.httpCore)
    api(projects.kspExt)
    api(projects.route.routeService)

    api(libs.ksp)
}
