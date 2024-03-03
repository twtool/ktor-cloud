package icu.twtool.ktor.cloud.redis

import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

private const val ReleaseLockScript = """
if redis.call('get', KEYS[1]) == ARGV[1] then
    return redis.call('del', KEYS[1])
else
    return 0
end  
"""

private const val ReentrantLockScript = """
if redis.call('get', KEYS[1]) == ARGV[1] then
    return redis.call('expire', KEYS[1], ARGV[2])
else
    return 0
end  
"""

object LockIdentifierKey : CoroutineContext.Key<LockElement>

data class LockElement(val identifier: String = UUID.randomUUID().toString()) : CoroutineContext.Element {

    override val key: CoroutineContext.Key<*> = LockIdentifierKey
}

/**
 * 获取锁后执行 action
 * @param key 锁的 key
 * @param duration 重试间隔
 * @param retryCount 重试次数
 * @param timeout 超时时间（锁占有的时间）
 * @param action 执行的 action
 */
suspend fun <T> Redis.lock(
    key: String,
    duration: Duration = 100.milliseconds,
    retryCount: Int = 10,
    timeout: Long = (duration * retryCount * 2).inWholeSeconds,
    action: suspend () -> T
): T {

    val element = coroutineContext[LockIdentifierKey]?.let {
        val res = jedis.eval(
            ReentrantLockScript, mutableListOf(key),
            mutableListOf(it.identifier, timeout.toString())
        )
        if (res is Number && res.toInt() > 0) {
            // 续期
            jedis.expire(key, timeout)
            return action()
        }
        it
    } ?: LockElement()

    // 尝试获取锁
    repeat(retryCount + 1) {
        if (jedis.setnx(key, element.identifier) == 1L) {
            jedis.expire(key, timeout)
            try {
                return withContext(coroutineContext + element) {
                    action()
                }
            } finally {
                // 释放锁
                jedis.eval(ReleaseLockScript, mutableListOf(key), mutableListOf(element.identifier))
            }
        }
        delay(duration)
    }
    error("Failed to acquire the lock.")
}