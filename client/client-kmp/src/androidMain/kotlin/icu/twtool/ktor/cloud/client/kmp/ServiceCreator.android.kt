package icu.twtool.ktor.cloud.client.kmp

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp

actual fun engine(): HttpClientEngine = OkHttp.create()