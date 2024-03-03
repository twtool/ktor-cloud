package icu.twtool.ktor.cloud.discovery.core

data class ServiceInstance(
    val name: String,
    val protocol: String,
    val host: String,
    val port: Int
)
