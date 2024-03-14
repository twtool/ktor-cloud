package icu.twtool.ktor.cloud.plugin.rocketmq

import icu.twtool.ktor.cloud.config.core.ConfigKey

val RocketMQEndpointKey = ConfigKey("rocketmq.endpoint", "localhost:8081", false)

val ConsumerGroupKey = ConfigKey<String>("rocketmq.consumer.group", nullable = false)
