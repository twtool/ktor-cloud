package sample.route.gateway

import icu.twtool.ktor.cloud.KtorCloudApplication
import icu.twtool.ktor.cloud.client.service.ServiceCreator
import icu.twtool.ktor.cloud.client.service.getService
import icu.twtool.ktor.cloud.discovery.polaris.PolarisRegistry
import icu.twtool.ktor.cloud.route.gateway.route
import io.ktor.server.application.call
import io.ktor.server.netty.Netty
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import sample.http.TestService
import sample.http.create

fun main() {

    KtorCloudApplication.start(Netty) {

        install(PolarisRegistry())
        install(ServiceCreator(listOf(TestService::create)))

        route(TestService::class)

        routing.get("/t") {
            call.respond(getService<TestService>().test())
        }
    }
}