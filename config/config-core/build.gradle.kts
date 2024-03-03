plugins {
    alias(libs.plugins.kotlinJvm)

    `maven-publish`
}

dependencies {
    api(libs.slf4j.api)
    api(libs.snakeyaml)
}