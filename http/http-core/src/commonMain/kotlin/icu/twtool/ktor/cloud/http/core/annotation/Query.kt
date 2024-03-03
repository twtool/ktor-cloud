package icu.twtool.ktor.cloud.http.core.annotation

@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class Query(val name: String = "")
