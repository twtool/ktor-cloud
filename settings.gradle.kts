rootProject.name = "ktor-cloud"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    @Suppress("UnstableApiUsage")
    repositories {
        maven("https://mirrors.cloud.tencent.com/nexus/repository/maven-public")
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

include(":gradle-plugin")

include(":application")

include(":client:client-kmp")
include(":client:client-kmp-websocket")
include(":client:client-service")

include(":config:config-core")

include(":discovery:discovery-core")
include(":discovery:discovery-polaris")
include(":opentelemetry")

include(":http:http-core")
include(":http:http-ksp")

include(":route:route-gateway")
include(":route:route-service")
include(":route:route-service-ksp")
include(":route:route-websocket")

include(":ksp-ext")

include(":exposed")
include(":redis")

include(":plugins:plugin-rocketmq")

include(":samples:sample-http-ksp")
include(":samples:sample-route-gateway")
include(":samples:sample-route-service-ksp")