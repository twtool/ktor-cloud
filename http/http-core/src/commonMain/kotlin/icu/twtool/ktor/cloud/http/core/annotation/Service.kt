package icu.twtool.ktor.cloud.http.core.annotation

@Target(AnnotationTarget.CLASS)
annotation class Service(
    val name: String,
    val path: String,
)