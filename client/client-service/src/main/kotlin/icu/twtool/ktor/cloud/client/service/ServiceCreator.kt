package icu.twtool.ktor.cloud.client.service

import icu.twtool.ktor.cloud.KtorCloudApplication
import icu.twtool.ktor.cloud.Plugin
import icu.twtool.ktor.cloud.applicationOr
import icu.twtool.ktor.cloud.discovery.core.HttpClient
import icu.twtool.ktor.cloud.discovery.core.Registry
import icu.twtool.ktor.cloud.http.core.IServiceCreator
import icu.twtool.ktor.cloud.http.core.KtorCloudRequest
import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.URLProtocol
import io.ktor.http.appendPathSegments
import io.ktor.http.contentType
import io.ktor.server.application.ApplicationStarted
import io.ktor.server.application.createApplicationPlugin
import io.ktor.server.application.hooks.MonitoringEvent
import io.ktor.server.application.install
import io.ktor.util.AttributeKey
import kotlin.reflect.KClass

private val ServiceImplCacheKey = AttributeKey<Map<String, Any>>("ServiceImplCache")

@Suppress("UNCHECKED_CAST")
fun <T : Any> KtorCloudApplication.getService(kClass: KClass<T>): T =
    application.attributes[ServiceImplCacheKey][kClass.qualifiedName!!] as T

suspend fun <T : Any> getService(kClass: KClass<T>): T =
    applicationOr().getService(kClass)

suspend inline fun <reified T : Any> getService(): T = getService(T::class)
inline fun <reified T : Any> KtorCloudApplication.getService(): T = getService(T::class)

class ServiceCreator(
    private val services: List<(IServiceCreator) -> Any>
) : IServiceCreator, Plugin {

    private lateinit var application: KtorCloudApplication

    override suspend fun <T> request(request: KtorCloudRequest): T {
        with(application) {
            val instance = Registry.getInstance(request.serviceName) ?: error("${request.serviceName} unavailable.")

            return HttpClient.request {
                method = request.method

                request.headers.forEach(this::header)
                url {
                    protocol = URLProtocol.createOrDefault(instance.protocol)
                    host = instance.host
                    port = instance.port

                    appendPathSegments(request.path)
                    request.queries.forEach(parameters::append)
                }

                request.body?.let {
                    contentType(ContentType.Application.Json)
                    setBody(it.second, it.first)
                }
            }.body(request.returnType)
        }

    }

    override fun KtorCloudApplication.install() {
        this@ServiceCreator.application = this
        application.install(createApplicationPlugin("ServiceCreator") {
            on(MonitoringEvent(ApplicationStarted)) {
                it.attributes.put(
                    ServiceImplCacheKey, services.fold(mutableMapOf()) { acc, func ->
                        val service = func(this@ServiceCreator)
                        acc[service.toString()] = service
                        acc
                    }
                )
            }
        })
    }
}