package sample.route.service

import icu.twtool.ktor.cloud.KtorCloudApplication
import icu.twtool.ktor.cloud.discovery.polaris.PolarisRegistry
import icu.twtool.ktor.cloud.opentelemetry.OpenTelemetryPlugin
import io.ktor.server.netty.Netty

fun main() {
    KtorCloudApplication.start(Netty) {
        install(PolarisRegistry())
        OpenTelemetryPlugin.install()

        TestServiceImpl().register()
    }
}