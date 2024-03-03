plugins {
    alias(libs.plugins.kotlinJvm)

    `maven-publish`
}

dependencies {
    api(projects.application)
    api(projects.config.configCore)
    api(projects.discovery.discoveryCore)

    api(libs.polaris.discovery)
}