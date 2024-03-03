package icu.twtool.ktor.cloud.config.core

data class ConfigKey<T>(val key: String, val default: T? = null, val nullable: Boolean = true)
