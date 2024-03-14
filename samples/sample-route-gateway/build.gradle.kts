import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.kotlinJvm)
    application
}

application {
    mainClass.set("sample.route.gateway.ApplicationKt")

    applicationDefaultJvmArgs = listOf("-Dserver.port=8080")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xcontext-receivers"
    }
}

dependencies {
    api(projects.application)
    api(projects.client.clientService)
    api(projects.http.httpCore)
    api(projects.route.routeGateway)
    api(projects.discovery.discoveryPolaris)
    api(projects.samples.sampleHttpKsp)

    api(libs.ktor.server.netty)

//    ksp(projects.route.routeServiceKsp)
}