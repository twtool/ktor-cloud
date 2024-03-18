package icu.twtool.ktor.cloud.plugin.rocketmq

import org.apache.rocketmq.client.apis.message.Message
import org.apache.rocketmq.client.java.message.MessageBuilderImpl

fun buildMessage(
    topic: String,
    body: ByteArray,
    tag: String? = null,
    messageGroup: String? = null,
    vararg keys: String
): Message {
    return MessageBuilderImpl()
        .setTopic(topic)
        .setKeys(*keys)
        .run { tag?.let { setTag(it) } ?: this }
        .run { messageGroup?.let { setMessageGroup(it) } ?: this }
        .setBody(body)
        .build()
}