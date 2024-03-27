plugins {
    alias(libs.plugins.kotlinJvm)
    `maven-publish`
}

dependencies {
    api(projects.application)
    api(projects.config.configCore)
    api(projects.discovery.discoveryCore)

    api(libs.opentelemetry.api)
    api(libs.opentelemetry.sdk)
    api(libs.opentelemetry.exporter.otlp)
}