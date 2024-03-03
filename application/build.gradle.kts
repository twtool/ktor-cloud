plugins {
    alias(libs.plugins.kotlinJvm)
    `maven-publish`
}

dependencies {
    api(projects.config.configCore)

    api(libs.ktor.server.core)
    api(libs.ktor.server.host.common)
    api(libs.ktor.server.content.negotiation)
    api(libs.ktor.serialization.kotlinx.json)

    api(libs.logback)
}