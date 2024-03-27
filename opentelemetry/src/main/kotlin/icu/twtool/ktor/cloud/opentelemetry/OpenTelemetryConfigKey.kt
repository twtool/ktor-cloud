package icu.twtool.ktor.cloud.opentelemetry

import icu.twtool.ktor.cloud.config.core.configKey

val OpenTelemetryEndpointKey = configKey("opentelemetry.endpoint", "http://localhost:4317")