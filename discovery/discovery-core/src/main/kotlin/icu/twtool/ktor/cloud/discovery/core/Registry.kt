package icu.twtool.ktor.cloud.discovery.core

import icu.twtool.ktor.cloud.KtorCloudApplication
import icu.twtool.ktor.cloud.Plugin
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
        val client = createHttpClient()

        application.attributes.put(RegistryKey, this@Registry)
        application.attributes.put(HttpClientKey, client)
        register()
    }
}