package icu.twtool.ktor.cloud.redis

import redis.clients.jedis.args.ListDirection
import redis.clients.jedis.args.ListPosition
import redis.clients.jedis.params.LPosParams
import redis.clients.jedis.util.KeyValue
import java.security.Key

fun Redis.rpush(key: String, vararg strings: String): Long = jedis.rpush(key, *strings)
fun Redis.rpush(key: ByteArray, vararg strings: ByteArray): Long = jedis.rpush(key, *strings)

fun Redis.lpush(key: String, vararg strings: String): Long = jedis.lpush(key, *strings)
fun Redis.lpush(key: ByteArray, vararg strings: ByteArray): Long = jedis.lpush(key, *strings)

fun Redis.llen(key: String): Long = jedis.llen(key)
fun Redis.llen(key: ByteArray): Long = jedis.llen(key)

fun Redis.lrange(key: String, start: Long, stop: Long): List<String> = jedis.lrange(key, start, stop)
fun Redis.lrange(key: ByteArray, start: Long, stop: Long): List<ByteArray> = jedis.lrange(key, start, stop)

fun Redis.ltrim(key: String, start: Long, stop: Long): String = jedis.ltrim(key, start, stop)
fun Redis.ltrim(key: ByteArray, start: Long, stop: Long): String = jedis.ltrim(key, start, stop)

fun Redis.lindex(key: String, index: Long): String = jedis.lindex(key, index)
fun Redis.lindex(key: ByteArray, index: Long): ByteArray = jedis.lindex(key, index)

fun Redis.lset(key: String, index: Long, value: String): String = jedis.lset(key, index, value)
fun Redis.lset(key: ByteArray, index: Long, value: ByteArray): String = jedis.lset(key, index, value)

fun Redis.lrem(key: String, count: Long, value: String): Long = jedis.lrem(key, count, value)
fun Redis.lrem(key: ByteArray, count: Long, value: ByteArray): Long = jedis.lrem(key, count, value)

fun Redis.lpop(key: String): String = jedis.lpop(key)
fun Redis.lpop(key: ByteArray): ByteArray = jedis.lpop(key)
fun Redis.lpop(key: String, count: Int): List<String> = jedis.lpop(key, count)
fun Redis.lpop(key: ByteArray, count: Int): List<ByteArray> = jedis.lpop(key, count)

fun Redis.lpos(key: String, element: String): Long = jedis.lpos(key, element)
fun Redis.lpos(key: String, element: String, params: LPosParams): Long = jedis.lpos(key, element, params)
fun Redis.lpos(key: ByteArray, element: ByteArray): Long = jedis.lpos(key, element)
fun Redis.lpos(key: ByteArray, element: ByteArray, params: LPosParams): Long = jedis.lpos(key, element, params)

fun Redis.rpop(key: String): String = jedis.rpop(key)
fun Redis.rpop(key: ByteArray): ByteArray = jedis.rpop(key)
fun Redis.rpop(key: String, count: Int): List<String> = jedis.rpop(key, count)
fun Redis.rpop(key: ByteArray, count: Int): List<ByteArray> = jedis.rpop(key, count)

fun Redis.linsert(key: String, where: ListPosition, pivot: String, value: String): Long =
    jedis.linsert(key, where, pivot, value)

fun Redis.linsert(key: ByteArray, where: ListPosition, pivot: ByteArray, value: ByteArray): Long =
    jedis.linsert(key, where, pivot, value)

fun Redis.lpushx(key: String, vararg strings: String): Long = jedis.lpushx(key, *strings)
fun Redis.lpushx(key: ByteArray, vararg strings: ByteArray): Long = jedis.lpushx(key, *strings)

fun Redis.rpushx(key: String, vararg strings: String): Long = jedis.rpushx(key, *strings)
fun Redis.rpushx(key: ByteArray, vararg strings: ByteArray): Long = jedis.rpushx(key, *strings)

fun Redis.blpop(timeout: Int, key: String): List<String> = jedis.blpop(timeout, key)
fun Redis.blpop(timeout: Double, key: String): KeyValue<String, String> = jedis.blpop(timeout, key)
fun Redis.blpop(timeout: Int, vararg keys: String): List<String> = jedis.blpop(timeout, *keys)
fun Redis.blpop(timeout: Double, vararg keys: String): KeyValue<String, String> = jedis.blpop(timeout, *keys)
fun Redis.blpop(timeout: Int, key: ByteArray): List<ByteArray> = jedis.blpop(timeout, key)
fun Redis.blpop(timeout: Double, key: ByteArray): KeyValue<ByteArray, ByteArray> = jedis.blpop(timeout, key)
fun Redis.blpop(timeout: Int, vararg keys: ByteArray): List<ByteArray> = jedis.blpop(timeout, *keys)
fun Redis.blpop(timeout: Double, vararg keys: ByteArray): KeyValue<ByteArray, ByteArray> = jedis.blpop(timeout, *keys)

fun Redis.brpop(timeout: Int, key: String): List<String> = jedis.brpop(timeout, key)
fun Redis.brpop(timeout: Double, key: String): KeyValue<String, String> = jedis.brpop(timeout, key)
fun Redis.brpop(timeout: Int, vararg keys: String): List<String> = jedis.brpop(timeout, *keys)
fun Redis.brpop(timeout: Double, vararg keys: String): KeyValue<String, String> = jedis.brpop(timeout, *keys)
fun Redis.brpop(timeout: Int, key: ByteArray): List<ByteArray> = jedis.brpop(timeout, key)
fun Redis.brpop(timeout: Double, key: ByteArray): KeyValue<ByteArray, ByteArray> = jedis.brpop(timeout, key)
fun Redis.brpop(timeout: Int, vararg keys: ByteArray): List<ByteArray> = jedis.brpop(timeout, *keys)
fun Redis.brpop(timeout: Double, vararg keys: ByteArray): KeyValue<ByteArray, ByteArray> = jedis.brpop(timeout, *keys)

fun Redis.rpoplpush(srcKey: String, dstKey: String): String = jedis.rpoplpush(srcKey, dstKey)
fun Redis.rpoplpush(srcKey: ByteArray, dstKey: ByteArray): ByteArray = jedis.rpoplpush(srcKey, dstKey)

fun Redis.brpoplpush(source: String, destination: String, timeout: Int): String =
    jedis.brpoplpush(source, destination, timeout)

fun Redis.brpoplpush(source: ByteArray, destination: ByteArray, timeout: Int): ByteArray =
    jedis.brpoplpush(source, destination, timeout)

fun Redis.lmove(srcKey: String, dstKey: String, from: ListDirection, to: ListDirection): String =
    jedis.lmove(srcKey, dstKey, from, to)

fun Redis.lmove(srcKey: ByteArray, dstKey: ByteArray, from: ListDirection, to: ListDirection): ByteArray =
    jedis.lmove(srcKey, dstKey, from, to)

fun Redis.blmove(srcKey: String, dstKey: String, from: ListDirection, to: ListDirection, timeout: Double): String =
    jedis.blmove(srcKey, dstKey, from, to, timeout)

fun Redis.blmove(
    srcKey: ByteArray,
    dstKey: ByteArray,
    from: ListDirection,
    to: ListDirection,
    timeout: Double
): ByteArray =
    jedis.blmove(srcKey, dstKey, from, to, timeout)

fun Redis.lmpop(direction: ListDirection, vararg keys: String): KeyValue<String, List<String>> =
    jedis.lmpop(direction, *keys)

fun Redis.lmpop(direction: ListDirection, vararg keys: ByteArray): KeyValue<ByteArray, List<ByteArray>> =
    jedis.lmpop(direction, *keys)

fun Redis.lmpop(direction: ListDirection, count: Int, vararg keys: String): KeyValue<String, List<String>> =
    jedis.lmpop(direction, count, *keys)

fun Redis.lmpop(direction: ListDirection, count: Int, vararg keys: ByteArray): KeyValue<ByteArray, List<ByteArray>> =
    jedis.lmpop(direction, count, *keys)

fun Redis.blmpop(timeout: Double, direction: ListDirection, vararg keys: String): KeyValue<String, List<String>> =
    jedis.blmpop(timeout, direction, *keys)

fun Redis.blmpop(
    timeout: Double,
    direction: ListDirection,
    vararg keys: ByteArray
): KeyValue<ByteArray, List<ByteArray>> =
    jedis.blmpop(timeout, direction, *keys)

fun Redis.blmpop(
    timeout: Double,
    direction: ListDirection,
    count: Int,
    vararg keys: String
): KeyValue<String, List<String>> =
    jedis.blmpop(timeout, direction, count, *keys)

fun Redis.blmpop(
    timeout: Double,
    direction: ListDirection,
    count: Int,
    vararg keys: ByteArray
): KeyValue<ByteArray, List<ByteArray>> =
    jedis.blmpop(timeout, direction, count, *keys)

