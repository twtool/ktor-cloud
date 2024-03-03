package icu.twtool.ktor.cloud.client.kmp

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.java.Java

actual fun engine(): HttpClientEngine = Java.create()