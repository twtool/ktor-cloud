package icu.twtool.ktor.cloud.config.core

import org.slf4j.LoggerFactory
import org.yaml.snakeyaml.Yaml
import java.io.FileInputStream

class KtorCloudConfiguration {

    private val log = LoggerFactory.getLogger(KtorCloudConfiguration::class.java)

    private var config: Map<String, Any> = hashMapOf()

    fun load() {
        val fileName = "cloud.yaml"

        try {
            config = Yaml().load(
                KtorCloudConfiguration::class.java.classLoader?.getResourceAsStream(fileName) ?: FileInputStream(
                    fileName
                )
            )
        } catch (e: Exception) {
            log.warn(e.message)
            return
        }
        log.info("load cloud.yaml success.")
        if (log.isDebugEnabled) log.debug("current config: {}", config)
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T> get(key: String, default: T? = null, map: Map<String, Any>): T? {
        val index = key.indexOf('.')
        return if (index == -1) (map[key] ?: default) as T?
        else get(
            key.substring(index + 1),
            default,
            (map[key.substring(0, index)] ?: return default) as Map<String, Any>
        )
    }

    operator fun <T> get(key: String, default: T? = null, nullable: Boolean = true): T = get(key, default, config).run {
        check(nullable || this != null) { "$key is not nullable." }

        @Suppress("UNCHECKED_CAST")
        this as T
    }

    operator fun <T> get(ck: ConfigKey<T>): T = get(ck.key, ck.default, ck.nullable)
}