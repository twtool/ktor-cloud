package icu.twtool.ktor.cloud

import icu.twtool.ktor.cloud.config.core.ConfigKey
import icu.twtool.ktor.cloud.config.core.configKey


val HostKey: ConfigKey<String> = configKey("server.host", "0.0.0.0", false)
val PortKey: ConfigKey<Int> = configKey("server.port", 8080, false)
