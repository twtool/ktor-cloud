package icu.twtool.ktor.cloud.discovery.core

import icu.twtool.ktor.cloud.KtorCloudApplication
import icu.twtool.ktor.cloud.Plugin
import io.ktor.server.application.ApplicationStarted
import io.ktor.server.application.createApplicationPlugin
import io.ktor.server.application.hooks.MonitoringEvent
import io.ktor.server.application.install
import io.ktor.util.AttributeKey

private val RegistryKey = AttributeKey<Registry>("Registry")

abstract class Registry : Plugin {

    companion object {

        context(KtorCloudApplication)
        fun getInstance(serviceName: String): ServiceInstance? =
            application.attributes[RegistryKey].getInstance(serviceName)
    }

    abstract fun KtorCloudApplication.register()
    abstract fun getInstance(serviceName: String): ServiceInstance?

    override fun KtorCloudApplication.install() {
        application.install(createApplicationPlugin("Discovery") {
            on(MonitoringEvent(ApplicationStarted)) {
                val client = createHttpClient()

                it.attributes.put(RegistryKey, this@Registry)
                it.attributes.put(HttpClientKey, client)
                register()
            }
        })
    }
}