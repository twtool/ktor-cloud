package icu.twtool.ktor.cloud.redis

import icu.twtool.ktor.cloud.KtorCloudApplication
import icu.twtool.ktor.cloud.applicationOr
import io.ktor.util.AttributeKey
import redis.clients.jedis.JedisPooled

private val RedisKey = AttributeKey<Redis>("icu.twtool.ktor.cloud.redis.key")

val KtorCloudApplication.redis: Redis get() = application.attributes[RedisKey]

suspend fun redis(): Redis = applicationOr().redis

fun KtorCloudApplication.initRedis() {
    val redis = Redis(
        config[RedisHostKey],
        config[RedisPortKey]
    )
    application.attributes.put(
        RedisKey,
        redis
    )
}

class Redis(host: String, port: Int) {

    val jedis: JedisPooled = JedisPooled(host, port)
}