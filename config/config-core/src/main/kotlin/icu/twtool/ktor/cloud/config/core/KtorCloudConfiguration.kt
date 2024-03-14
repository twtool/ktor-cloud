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

    @Suppress("UNCHECKED_CAST")
    operator fun <T : Any> get(
        key: String,
        default: T? = null,
        nullable: Boolean = true,
        convert: (String) -> T = { it as T }
    ): T {
        if (log.isDebugEnabled)
            log.debug("read config: key = {}", key)

        return (System.getProperty(key)?.let {
            convert(it)
        } ?: (get(key, default, config).run {
            check(nullable || this != null) { "$key is not nullable." }

            this as T
        })).apply {
            if (log.isDebugEnabled)
                log.debug("read config: {} = {}, type = {}", key, this, this::class)
        }
    }

    operator fun <T : Any> get(ck: ConfigKey<T>): T = get(ck.key, ck.default, ck.nullable, ck.convert)
}