package icu.twtool.ktor.cloud.redis

import icu.twtool.ktor.cloud.config.core.ConfigKey

val RedisHostKey = ConfigKey("redis.host", "localhost")
val RedisPortKey = ConfigKey("redis.port", 6379)