package icu.twtool.ktor.cloud.discovery.core

import icu.twtool.ktor.cloud.config.core.ConfigKey

val ServiceName = ConfigKey<String>("service.name", nullable = false)
val ServiceProtocolKey = ConfigKey("service.protocol", "http")