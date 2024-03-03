package sample.route.service

import icu.twtool.ktor.cloud.KtorCloudApplication
import icu.twtool.ktor.cloud.discovery.polaris.PolarisRegistry
import io.ktor.server.netty.Netty

fun main() {
    KtorCloudApplication.start(Netty) {
        TestServiceImpl().register()

        install(PolarisRegistry())
    }
}