package icu.twtool.ktor.cloud.route.websockets

import icu.twtool.ktor.cloud.KtorCloudApplication
import io.ktor.server.websocket.DefaultWebSocketServerSession
import io.ktor.server.websocket.webSocket

fun KtorCloudApplication.websocket(path: String, handler: suspend DefaultWebSocketServerSession.() -> Unit) {
    routing.webSocket(path, handler = handler)
}