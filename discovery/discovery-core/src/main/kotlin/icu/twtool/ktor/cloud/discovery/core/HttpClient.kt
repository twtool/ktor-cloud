package icu.twtool.ktor.cloud.discovery.core

import icu.twtool.ktor.cloud.KtorCloudApplication
import io.ktor.client.HttpClient
import io.ktor.client.engine.java.Java
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.AttributeKey


internal val HttpClientKey = AttributeKey<HttpClient>("HttpClient")

val KtorCloudApplication.HttpClient: HttpClient get() = application.attributes[HttpClientKey]

fun createHttpClient() = HttpClient(Java) {
    install(ContentNegotiation) {
        json()
    }
}