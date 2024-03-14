package icu.twtool.ktor.cloud.config.core

data class ConfigKey<T>(
    val key: String, val default: T? = null, val nullable: Boolean = true,
    val convert: (String) -> T = {
        @Suppress("UNCHECKED_CAST")
        it as T
    }
)

inline fun <reified T> configKey(key: String, default: T? = null, nullable: Boolean = true): ConfigKey<T> {
    println(key + " : " + T::class)
    val convert: (String) -> T = when (T::class) {
        String::class -> ({ it as T })
        Int::class -> ({ it.toInt() as T })
        Long::class -> ({ it.toLong() as T })
        Float::class -> ({ it.toFloat() as T })
        Double::class -> ({ it.toDouble() as T })
        else -> ({ it as T })
    }
    return ConfigKey(key, default, nullable, convert)
}
