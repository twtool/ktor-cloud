package icu.twtool.ktor.cloud.client.kmp.websocket

import icu.twtool.ktor.cloud.client.kmp.ServiceCreator
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.url
import io.ktor.http.URLProtocol
import io.ktor.http.encodedPath
import io.ktor.http.path
import kotlin.time.Duration

fun HttpClientConfig<*>.websocket() {
    install(WebSockets) {
        pingInterval = 20_000
    }
}

suspend fun ServiceCreator.websocket(path: String): DefaultClientWebSocketSession {
    return client.webSocketSession {
        url {
            protocol = this@websocket.websocketProtocol
            host = this@websocket.host
            port = this@websocket.port

            encodedPath = path
        }
    }
}