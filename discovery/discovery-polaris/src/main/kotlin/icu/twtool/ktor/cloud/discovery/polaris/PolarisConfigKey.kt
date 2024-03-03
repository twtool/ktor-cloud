package icu.twtool.ktor.cloud.discovery.polaris

import icu.twtool.ktor.cloud.config.core.ConfigKey

val AddressesKey = ConfigKey("polaris.addresses", listOf("localhost:8091"))
val NamespaceKey = ConfigKey("polaris.namespace", "default")

