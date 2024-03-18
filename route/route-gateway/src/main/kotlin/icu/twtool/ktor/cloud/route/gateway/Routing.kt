package icu.twtool.ktor.cloud.route.gateway

import icu.twtool.ktor.cloud.KtorCloudApplication
import icu.twtool.ktor.cloud.discovery.core.HttpClient
import icu.twtool.ktor.cloud.discovery.core.Registry
import icu.twtool.ktor.cloud.http.core.annotation.InternalOnly
import icu.twtool.ktor.cloud.http.core.annotation.RequestMapping
import icu.twtool.ktor.cloud.http.core.annotation.Service
import icu.twtool.ktor.cloud.route.gateway.exception.NotAllowedAcceptInternalException
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsChannel
import io.ktor.http.URLProtocol
import io.ktor.http.appendPathSegments
import io.ktor.server.application.ApplicationCallPipeline
import io.ktor.server.application.call
import io.ktor.server.request.httpMethod
import io.ktor.server.request.receiveChannel
import io.ktor.server.response.header
import io.ktor.server.response.respond
import io.ktor.server.routing.route
import kotlin.reflect.KClass
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation

fun KtorCloudApplication.route(service: KClass<out Any>) {
    val anno = service.findAnnotation<Service>() ?: error("Could not find the @Service annotation on $service.")

    val internalPath = service.declaredFunctions.filter { it.hasAnnotation<InternalOnly>() }
        .mapNotNull { it.findAnnotation<RequestMapping>()?.path }

    routing.route("${anno.path}/{param...}") {
        handle {
            val paths = call.parameters.getAll("param")!!
            if (internalPath.contains(paths.joinToString("/"))) {
                throw NotAllowedAcceptInternalException()
            }
            val instance = Registry.getInstance(anno.name) ?: error("${anno.name} unavailable.")

            val response = HttpClient.request {
                method = call.request.httpMethod
                url {
                    protocol = URLProtocol.createOrDefault(instance.protocol)
                    host = instance.host
                    port = instance.port

                    appendPathSegments(paths)
                    parameters.appendAll(call.request.queryParameters)
                }

                headers.appendAll(call.request.headers)

                setBody(call.receiveChannel())
            }
            call.response.status(response.status)
            response.headers.forEach { name, values ->
                values.forEach { value ->
                    call.response.header(name, value)
                }
            }
            call.respond(response.bodyAsChannel())
        }
    }
}