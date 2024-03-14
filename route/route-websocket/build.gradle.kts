plugins {
    alias(libs.plugins.kotlinJvm)

    `maven-publish`
}

dependencies {
    api(projects.application)
    api(libs.ktor.server.websockets)
}