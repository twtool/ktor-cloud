package icu.twtool.ktor.cloud.plugin.rocketmq

import icu.twtool.ktor.cloud.KtorCloudApplication
import icu.twtool.ktor.cloud.Plugin
import io.ktor.util.AttributeKey
import org.apache.rocketmq.client.apis.ClientConfiguration
import org.apache.rocketmq.client.apis.ClientServiceProvider
import org.apache.rocketmq.client.apis.consumer.ConsumeResult
import org.apache.rocketmq.client.apis.consumer.FilterExpression
import org.apache.rocketmq.client.apis.consumer.PushConsumer
import org.apache.rocketmq.client.apis.message.MessageView
import org.apache.rocketmq.client.apis.producer.Producer

val RocketMQPluginKey =
    AttributeKey<RocketMQPlugin>("icu.twtool.ktor.cloud.rocketmq.plugin")

class RocketMQPlugin : Plugin {

    private val provider = ClientServiceProvider.loadService()
    private lateinit var configuration: ClientConfiguration
    private lateinit var consumerGroup: String

    override fun KtorCloudApplication.install() {
        configuration = ClientConfiguration.newBuilder()
            .setEndpoints(config[RocketMQEndpointKey])
            .build()

        consumerGroup = config[ConsumerGroupKey]

        application.attributes.put(RocketMQPluginKey, this@RocketMQPlugin)

    }

    fun getProducer(topics: Array<String> = emptyArray(), maxAttempts: Int = 3): Producer =
        provider.newProducerBuilder()
            .setClientConfiguration(configuration)
            .setMaxAttempts(maxAttempts)
            .setTopics(*topics)
            .build()

    /**
     * @param expressions 要订阅的表达
     * @param group 默认为 [consumerGroup]，传入则为 "${$consumerGroup$group}"
     * @param maxCacheMessageCount 本地缓存的最大消息数 [org.apache.rocketmq.client.java.impl.consumer.PushConsumerBuilderImpl.maxCacheMessageCount]。
     * @param maxCacheMessageSizeInBytes 设置本地缓存消息的最大字节数。 [org.apache.rocketmq.client.java.impl.consumer.PushConsumerBuilderImpl.maxCacheMessageSizeInBytes]
     * @param consumptionThreadCount 设置并行消费线程数。[org.apache.rocketmq.client.java.impl.consumer.PushConsumerBuilderImpl.consumptionThreadCount]
     */
    fun getPushConsumer(
        expressions: Map<String, FilterExpression>,
        group: String = "",
        maxCacheMessageCount: Int = 1024,
        maxCacheMessageSizeInBytes: Int = 64 * 1024 * 1024,
        consumptionThreadCount: Int = 20,
        listener: (MessageView) -> ConsumeResult
    ): PushConsumer =
        provider.newPushConsumerBuilder()
            .setClientConfiguration(configuration)
            .setConsumerGroup("$consumerGroup$group")
            .setSubscriptionExpressions(expressions)
            .setMaxCacheMessageCount(maxCacheMessageCount)
            .setMaxCacheMessageSizeInBytes(maxCacheMessageSizeInBytes)
            .setConsumptionThreadCount(consumptionThreadCount)
            .setMessageListener(listener)
            .build()


    companion object {

        context(KtorCloudApplication)
        fun install() {
            install(RocketMQPlugin())
        }

        context(KtorCloudApplication)
        fun getInstance(): RocketMQPlugin = application.attributes[RocketMQPluginKey]
    }
}