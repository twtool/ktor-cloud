package icu.twtool.ktor.cloud.redis

import redis.clients.jedis.args.BitCountOption
import redis.clients.jedis.args.BitOP
import redis.clients.jedis.params.BitPosParams
import redis.clients.jedis.params.GetExParams
import redis.clients.jedis.params.LCSParams
import redis.clients.jedis.params.SetParams
import redis.clients.jedis.resps.LCSMatchResult

// String Commands

fun Redis.set(key: String, value: String, params: SetParams? = null): String =
    if (params == null) jedis.set(key, value) else jedis.set(key, value, params)

fun Redis.set(key: ByteArray, value: ByteArray, params: SetParams? = null): String =
    if (params == null) jedis.set(key, value) else jedis.set(key, value, params)

fun Redis.get(key: String): String =
    jedis.get(key)

fun Redis.get(key: ByteArray): ByteArray =
    jedis.get(key)

fun Redis.setGet(key: String, value: String, params: SetParams? = null): String =
    if (params == null) jedis.setGet(key, value) else jedis.setGet(key, value, params)

fun Redis.setGet(key: ByteArray, value: ByteArray, params: SetParams? = null): ByteArray =
    if (params == null) jedis.setGet(key, value) else jedis.setGet(key, value, params)

fun Redis.getDel(key: String): String =
    jedis.getDel(key)

fun Redis.getDel(key: ByteArray): ByteArray =
    jedis.getDel(key)

fun Redis.getEx(key: String, params: GetExParams): String =
    jedis.getEx(key, params)

fun Redis.getEx(key: ByteArray, params: GetExParams): ByteArray =
    jedis.getEx(key, params)

fun Redis.setBit(key: String, offset: Long, value: Boolean): Boolean =
    jedis.setbit(key, offset, value)

fun Redis.setBit(key: ByteArray, offset: Long, value: Boolean): Boolean =
    jedis.setbit(key, offset, value)

fun Redis.getBit(key: String, offset: Long): Boolean =
    jedis.getbit(key, offset)

fun Redis.getBit(key: ByteArray, offset: Long): Boolean =
    jedis.getbit(key, offset)

fun Redis.setRange(key: String, offset: Long, value: String): Long =
    jedis.setrange(key, offset, value)

fun Redis.setRange(key: ByteArray, offset: Long, value: ByteArray): Long =
    jedis.setrange(key, offset, value)

fun Redis.getRange(key: String, startOffset: Long, endOffset: Long): String =
    jedis.getrange(key, startOffset, endOffset)

fun Redis.getRange(key: ByteArray, startOffset: Long, endOffset: Long): ByteArray =
    jedis.getrange(key, startOffset, endOffset)

fun Redis.getSet(key: String, value: String): String =
    jedis.getSet(key, value)

fun Redis.getSet(key: ByteArray, value: ByteArray): ByteArray =
    jedis.getSet(key, value)

fun Redis.setnx(key: String, value: String): Long =
    jedis.setnx(key, value)

fun Redis.setnx(key: ByteArray, value: ByteArray): Long =
    jedis.setnx(key, value)

fun Redis.setex(key: String, seconds: Long, value: String): String =
    jedis.setex(key, seconds, value)

fun Redis.setex(key: ByteArray, seconds: Long, value: ByteArray): String =
    jedis.setex(key, seconds, value)

fun Redis.psetex(key: String, milliseconds: Long, value: String): String =
    jedis.psetex(key, milliseconds, value)

fun Redis.psetex(key: ByteArray, milliseconds: Long, value: ByteArray): String =
    jedis.psetex(key, milliseconds, value)

fun Redis.incr(key: String): Long =
    jedis.incr(key)

fun Redis.incr(key: ByteArray): Long =
    jedis.incr(key)

fun Redis.incrBy(key: String, increment: Long): Long =
    jedis.incrBy(key, increment)

fun Redis.incrBy(key: ByteArray, increment: Long): Long =
    jedis.incrBy(key, increment)

fun Redis.incrByFloat(key: String, increment: Double): Double =
    jedis.incrByFloat(key, increment)

fun Redis.incrByFloat(key: ByteArray, increment: Double): Double =
    jedis.incrByFloat(key, increment)

fun Redis.decr(key: String): Long =
    jedis.decr(key)

fun Redis.decr(key: ByteArray): Long =
    jedis.decr(key)

fun Redis.decrBy(key: ByteArray, decrement: Long): Long =
    jedis.decrBy(key, decrement)

fun Redis.mget(vararg keys: String): List<String> =
    jedis.mget(*keys)

fun Redis.mget(vararg keys: ByteArray): List<ByteArray> =
    jedis.mget(*keys)

fun Redis.mset(vararg keysAndValues: String): String =
    jedis.mset(*keysAndValues)

fun Redis.mset(vararg keysAndValues: ByteArray): String =
    jedis.mset(*keysAndValues)

fun Redis.msetnx(vararg keysAndValues: String): Long =
    jedis.msetnx(*keysAndValues)

fun Redis.msetnx(vararg keysAndValues: ByteArray): Long =
    jedis.msetnx(*keysAndValues)

fun Redis.append(key: String, value: String): Long =
    jedis.append(key, value)

fun Redis.append(key: ByteArray, value: ByteArray): Long =
    jedis.append(key, value)

fun Redis.substr(key: String, start: Int, end: Int): String =
    jedis.substr(key, start, end)

fun Redis.substr(key: ByteArray, start: Int, end: Int): ByteArray =
    jedis.substr(key, start, end)

fun Redis.strlen(key: String): Long =
    jedis.strlen(key)

fun Redis.strlen(key: ByteArray): Long =
    jedis.strlen(key)

fun Redis.bitCount(key: String): Long =
    jedis.bitcount(key)

fun Redis.bitCount(key: ByteArray): Long =
    jedis.bitcount(key)

fun Redis.bitCount(key: String, range: Pair<Long, Long>, option: BitCountOption? = null): Long =
    if (option == null) jedis.bitcount(key, range.first, range.second)
    else jedis.bitcount(key, range.first, range.second, option)

fun Redis.bitCount(key: ByteArray, range: Pair<Long, Long>, option: BitCountOption? = null): Long =
    if (option == null) jedis.bitcount(key, range.first, range.second)
    else jedis.bitcount(key, range.first, range.second, option)

fun Redis.bitpos(key: String, value: Boolean, params: BitPosParams? = null): Long =
    if (params == null) jedis.bitpos(key, value) else jedis.bitpos(key, value, params)

fun Redis.bitpos(key: ByteArray, value: Boolean, params: BitPosParams? = null): Long =
    if (params == null) jedis.bitpos(key, value) else jedis.bitpos(key, value, params)

fun Redis.bitField(key: String, vararg arguments: String): List<Long> =
    jedis.bitfield(key, *arguments)

fun Redis.bitField(key: ByteArray, vararg arguments: ByteArray): List<Long> =
    jedis.bitfield(key, *arguments)

fun Redis.bitFieldReadonly(key: String, vararg arguments: String): List<Long> =
    jedis.bitfieldReadonly(key, *arguments)

fun Redis.bitFieldReadonly(key: ByteArray, vararg arguments: ByteArray): List<Long> =
    jedis.bitfieldReadonly(key, *arguments)

fun Redis.bitop(op: BitOP, destKey: String, vararg srcKey: String): Long =
    jedis.bitop(op, destKey, *srcKey)

fun Redis.bitop(op: BitOP, destKey: ByteArray, vararg srcKey: ByteArray): Long =
    jedis.bitop(op, destKey, *srcKey)

fun Redis.lcs(keyA: String, keyB: String, params: LCSParams?): LCSMatchResult =
    jedis.lcs(keyA, keyB, params)

fun Redis.lcs(keyA: ByteArray, keyB: ByteArray, params: LCSParams?): LCSMatchResult =
    jedis.lcs(keyA, keyB, params)