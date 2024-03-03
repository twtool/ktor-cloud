package icu.twtool.ktor.cloud.http.core.annotation

import icu.twtool.ktor.cloud.http.core.HttpMethod

@Target(AnnotationTarget.FUNCTION)
annotation class RequestMapping(
    val method: HttpMethod,
    val path: String,
)
