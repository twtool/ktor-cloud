package icu.twtool.ktor.cloud.plugin.rocketmq

import org.apache.rocketmq.client.apis.consumer.FilterExpression
import org.apache.rocketmq.client.apis.consumer.FilterExpressionType

fun filterTag(expression: String = "*") = FilterExpression("*", FilterExpressionType.TAG)