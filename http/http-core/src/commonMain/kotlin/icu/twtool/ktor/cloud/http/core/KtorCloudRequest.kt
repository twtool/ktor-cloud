package icu.twtool.ktor.cloud.http.core

import io.ktor.http.HttpMethod
import io.ktor.util.reflect.TypeInfo

data class KtorCloudRequest(
    val serviceName: String,
    val servicePath: String,
    val method: HttpMethod,
    val path: String,
    val headers: Map<String, Any> = emptyMap(),
    val queries: Map<String, String> = emptyMap(),
    val body: Pair<TypeInfo, Any>? = null,
    val returnType: TypeInfo
)
