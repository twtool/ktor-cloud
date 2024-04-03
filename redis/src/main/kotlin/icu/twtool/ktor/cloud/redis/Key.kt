package icu.twtool.ktor.cloud.redis

import redis.clients.jedis.ScanIteration
import redis.clients.jedis.args.ExpiryOption
import redis.clients.jedis.params.RestoreParams
import redis.clients.jedis.params.ScanParams
import redis.clients.jedis.params.SortingParams
import redis.clients.jedis.resps.ScanResult

fun Redis.exists(key: String): Boolean = jedis.exists(key)
fun Redis.exists(key: ByteArray): Boolean = jedis.exists(key)
fun Redis.exists(vararg keys: String): Long = jedis.exists(*keys)
fun Redis.exists(vararg keys: ByteArray): Long = jedis.exists(*keys)

fun Redis.persist(key: String): Long = jedis.persist(key)
fun Redis.persist(key: ByteArray): Long = jedis.persist(key)

fun Redis.type(key: String): String = jedis.type(key)
fun Redis.type(key: ByteArray): String = jedis.type(key)

fun Redis.dump(key: String): ByteArray = jedis.dump(key)
fun Redis.dump(key: ByteArray): ByteArray = jedis.dump(key)

fun Redis.restore(key: String, ttl: Long, serializedValue: ByteArray, params: RestoreParams? = null): String =
    if (params == null) jedis.restore(key, ttl, serializedValue) else jedis.restore(key, ttl, serializedValue, params)

fun Redis.restore(key: ByteArray, ttl: Long, serializedValue: ByteArray, params: RestoreParams? = null): String =
    if (params == null) jedis.restore(key, ttl, serializedValue) else jedis.restore(key, ttl, serializedValue, params)

fun Redis.expire(key: String, seconds: Long, option: ExpiryOption? = null): Long =
    if (option == null) jedis.expire(key, seconds) else jedis.expire(key, seconds, option)

fun Redis.expire(key: ByteArray, seconds: Long, option: ExpiryOption? = null): Long =
    if (option == null) jedis.expire(key, seconds) else jedis.expire(key, seconds, option)

fun Redis.pexpire(key: String, milliseconds: Long, option: ExpiryOption? = null): Long =
    if (option == null) jedis.expire(key, milliseconds) else jedis.expire(key, milliseconds, option)

fun Redis.pexpire(key: ByteArray, milliseconds: Long, option: ExpiryOption? = null): Long =
    if (option == null) jedis.expire(key, milliseconds) else jedis.expire(key, milliseconds, option)

fun Redis.expireTime(key: String): Long = jedis.expireTime(key)
fun Redis.expireTime(key: ByteArray): Long = jedis.expireTime(key)
fun Redis.pexpireTime(key: String): Long = jedis.pexpireTime(key)
fun Redis.pexpireTime(key: ByteArray): Long = jedis.pexpireTime(key)


fun Redis.expireAt(key: String, unixTime: Long, option: ExpiryOption? = null): Long =
    if (option == null) jedis.expireAt(key, unixTime) else jedis.expireAt(key, unixTime, option)

fun Redis.expireAt(key: ByteArray, unixTime: Long, option: ExpiryOption? = null): Long =
    if (option == null) jedis.expireAt(key, unixTime) else jedis.expireAt(key, unixTime, option)

fun Redis.pexpireAt(key: String, millisecondsTimestamp: Long, option: ExpiryOption? = null): Long =
    if (option == null) jedis.pexpireAt(key, millisecondsTimestamp)
    else jedis.pexpireAt(key, millisecondsTimestamp, option)

fun Redis.pexpireAt(key: ByteArray, millisecondsTimestamp: Long, option: ExpiryOption? = null): Long =
    if (option == null) jedis.pexpireAt(key, millisecondsTimestamp)
    else jedis.pexpireAt(key, millisecondsTimestamp, option)

fun Redis.ttl(key: String): Long = jedis.ttl(key)
fun Redis.ttl(key: ByteArray): Long = jedis.ttl(key)
fun Redis.pttl(key: String): Long = jedis.pttl(key)
fun Redis.pttl(key: ByteArray): Long = jedis.pttl(key)

fun Redis.touch(key: String): Long = jedis.touch(key)
fun Redis.touch(key: ByteArray): Long = jedis.touch(key)
fun Redis.touch(vararg keys: String): Long = jedis.touch(*keys)
fun Redis.touch(vararg keys: ByteArray): Long = jedis.touch(*keys)

fun Redis.sort(key: String, sortingParams: SortingParams? = null): List<String> =
    if (sortingParams == null) jedis.sort(key) else jedis.sort(key, sortingParams)

fun Redis.sort(key: ByteArray, sortingParams: SortingParams? = null): List<ByteArray> =
    if (sortingParams == null) jedis.sort(key) else jedis.sort(key, sortingParams)

fun Redis.sort(key: String, destKey: String, sortingParams: SortingParams? = null): Long =
    if (sortingParams == null) jedis.sort(key, destKey) else jedis.sort(key, sortingParams, destKey)

fun Redis.sort(key: ByteArray, deskKey: ByteArray, sortingParams: SortingParams? = null): Long =
    if (sortingParams == null) jedis.sort(key, deskKey) else jedis.sort(key, sortingParams, deskKey)

fun Redis.sortReadonly(key: String, sortingParams: SortingParams): List<String> =
    jedis.sortReadonly(key, sortingParams)

fun Redis.sortReadonly(key: ByteArray, sortingParams: SortingParams): List<ByteArray> =
    jedis.sortReadonly(key, sortingParams)

fun Redis.del(key: String): Long = jedis.del(key)
fun Redis.del(vararg keys: String): Long = jedis.del(*keys)
fun Redis.del(key: ByteArray): Long = jedis.del(key)
fun Redis.del(vararg keys: ByteArray): Long = jedis.del(*keys)

fun Redis.unlink(key: String): Long = jedis.unlink(key)
fun Redis.unlink(vararg keys: String): Long = jedis.unlink(*keys)
fun Redis.unlink(key: ByteArray): Long = jedis.unlink(key)
fun Redis.unlink(vararg keys: ByteArray): Long = jedis.unlink(*keys)

fun Redis.memoryUsage(key: String): Long = jedis.memoryUsage(key)
fun Redis.memoryUsage(key: String, samples: Int): Long = jedis.memoryUsage(key, samples)
fun Redis.memoryUsage(key: ByteArray): Long = jedis.memoryUsage(key)
fun Redis.memoryUsage(key: ByteArray, samples: Int): Long = jedis.memoryUsage(key, samples)

fun Redis.copy(srcKey: String, destKey: String, replace: Boolean): Boolean =
    jedis.copy(srcKey, destKey, replace)

fun Redis.copy(srcKey: ByteArray, destKey: ByteArray, replace: Boolean): Boolean =
    jedis.copy(srcKey, destKey, replace)

fun Redis.rename(oldKey: String, newKey: String): String = jedis.rename(oldKey, newKey)
fun Redis.rename(oldKey: ByteArray, newKey: ByteArray): String = jedis.rename(oldKey, newKey)

fun Redis.renamenx(oldKey: String, newKey: String): Long = jedis.renamenx(oldKey, newKey)
fun Redis.renamenx(oldKey: ByteArray, newKey: ByteArray): Long = jedis.renamenx(oldKey, newKey)

fun Redis.dbSize(): Long = jedis.dbSize()

fun Redis.keys(pattern: String): Set<String> = jedis.keys(pattern)
fun Redis.keys(pattern: ByteArray): Set<ByteArray> = jedis.keys(pattern)

fun Redis.scan(cursor: String, params: ScanParams? = null): ScanResult<String> =
    if (params == null) jedis.scan(cursor) else jedis.scan(cursor, params)

fun Redis.scan(cursor: ByteArray, params: ScanParams? = null): ScanResult<ByteArray> =
    if (params == null) jedis.scan(cursor) else jedis.scan(cursor, params)

fun Redis.scan(cursor: String, params: ScanParams, type: String): ScanResult<String> =
    jedis.scan(cursor, params, type)

fun Redis.scan(cursor: ByteArray, params: ScanParams, type: ByteArray): ScanResult<ByteArray> =
    jedis.scan(cursor, params, type)

fun Redis.scanIteration(batchCount: Int, match: String, type: String? = null): ScanIteration =
    if (type == null) jedis.scanIteration(batchCount, match) else jedis.scanIteration(batchCount, match, type)

fun Redis.randomKey(): String = jedis.randomKey()
fun Redis.randomBinaryKey(): ByteArray = jedis.randomBinaryKey()
