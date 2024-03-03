import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ksp)
    application
}

application {
    mainClass.set("sample.route.service.ApplicationKt")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xcontext-receivers"
    }
}

dependencies {
    api(projects.application)
    api(projects.http.httpCore)
    api(projects.route.routeService)
    api(projects.discovery.discoveryPolaris)
    api(projects.samples.sampleHttpKsp)

    api(libs.ktor.server.netty)

    ksp(projects.route.routeServiceKsp)
}