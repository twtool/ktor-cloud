package icu.twtool.ktor.cloud.route.websockets

import icu.twtool.ktor.cloud.KtorCloudApplication
import icu.twtool.ktor.cloud.Plugin
import io.ktor.server.application.install
import io.ktor.server.websocket.WebSockets

object WebSocketsPlugin : Plugin {

    override fun KtorCloudApplication.install() {
        application.install(WebSockets) {

        }
    }
}
