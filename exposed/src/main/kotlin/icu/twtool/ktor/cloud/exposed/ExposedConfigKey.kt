package icu.twtool.ktor.cloud.exposed

import icu.twtool.ktor.cloud.config.core.ConfigKey

val UrlKey = ConfigKey<String>("exposed.url", nullable = false)
val DriverKey = ConfigKey<String>("exposed.driver", nullable = false)
val UserKey = ConfigKey("exposed.user", "")
val PasswordKey = ConfigKey("exposed.password", "")