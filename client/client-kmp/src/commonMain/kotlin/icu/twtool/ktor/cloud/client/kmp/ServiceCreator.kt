package icu.twtool.ktor.cloud.client.kmp

import icu.twtool.ktor.cloud.http.core.IServiceCreator
import icu.twtool.ktor.cloud.http.core.KtorCloudRequest
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.header
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.URLProtocol
import io.ktor.http.appendPathSegments
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json

expect fun engine(): HttpClientEngine

class ServiceCreator(
    private val protocol: URLProtocol,
    val websocketProtocol: URLProtocol,
    val host: String,
    val port: Int,
    private val preHandlers: List<(HttpRequestBuilder) -> Unit> = emptyList(),
    private val configure: HttpClientConfig<*>.() -> Unit = {},
    private val throwableProcess: (Throwable) -> Any = { throw it }
) : IServiceCreator {

    val client by lazy {
        HttpClient(engine()) {
            install(ContentNegotiation) {
                json()
            }

            configure()
        }
    }

    private val cache = mutableMapOf<(IServiceCreator) -> Any, Any>()

    override suspend fun <T> request(request: KtorCloudRequest): T {
        val body = try {
            client.request {
                method = request.method

                request.headers.forEach(this::header)
                url {
                    protocol = this@ServiceCreator.protocol
                    host = this@ServiceCreator.host
                    port = this@ServiceCreator.port

                    appendPathSegments(request.servicePath, request.path)
                    request.queries.forEach(parameters::append)
                }

                request.body?.let {
                    contentType(ContentType.Application.Json)
                    setBody(it.second, it.first)
                }

                preHandlers.forEach {
                    it(this)
                }

            }.body<T>(request.returnType)
        } catch (e: Throwable) {
            throwableProcess(e)
        }
        @Suppress("UNCHECKED_CAST")
        return body as T
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> get(create: (IServiceCreator) -> T): T {
        return cache.getOrPut(create) { create(this) } as T
    }
}