package icu.twtool.ktor.cloud.redis

import redis.clients.jedis.args.SortedSetOption
import redis.clients.jedis.params.ZAddParams
import redis.clients.jedis.params.ZIncrByParams
import redis.clients.jedis.params.ZParams
import redis.clients.jedis.params.ZRangeParams
import redis.clients.jedis.resps.Tuple
import redis.clients.jedis.util.KeyValue

fun Redis.zadd(key: String, score: Double, member: String, params: ZAddParams? = null): Long =
    if (params == null) jedis.zadd(key, score, member) else jedis.zadd(key, score, member, params)

fun Redis.zadd(key: String, scroeMembers: Map<String, Double>, params: ZAddParams? = null): Long =
    if (params == null) jedis.zadd(key, scroeMembers) else jedis.zadd(key, scroeMembers, params)

fun Redis.zaddIncr(key: String, score: Double, member: String, params: ZAddParams): Double =
    jedis.zaddIncr(key, score, member, params)

fun Redis.zrem(key: String, vararg members: String): Long = jedis.zrem(key, *members)

fun Redis.zincrby(key: String, increment: Double, member: String, params: ZIncrByParams? = null): Double =
    if (params == null) jedis.zincrby(key, increment, member) else jedis.zincrby(key, increment, member, params)

fun Redis.zrank(key: String, member: String): Long = jedis.zrank(key, member)

fun Redis.zrevrank(key: String, member: String): Long = jedis.zrevrank(key, member)

fun Redis.zrankWithScore(key: String, member: String): KeyValue<Long, Double> = jedis.zrankWithScore(key, member)

fun Redis.zrevrankWithScore(key: String, member: String): KeyValue<Long, Double> = jedis.zrevrankWithScore(key, member)

fun Redis.zrange(key: String, start: Long, stop: Long): List<String> = jedis.zrange(key, start, stop)

fun Redis.zrevrange(key: String, start: Long, stop: Long): List<String> = jedis.zrevrange(key, start, stop)

fun Redis.zrangeWithScores(key: String, start: Long, stop: Long): List<Tuple> = jedis.zrangeWithScores(key, start, stop)

fun Redis.zrevrangeWithScores(key: String, start: Long, stop: Long): List<Tuple> =
    jedis.zrevrangeWithScores(key, start, stop)

fun Redis.zrange(key: String, zRangeParams: ZRangeParams): List<String> = jedis.zrange(key, zRangeParams)

fun Redis.zrangeWithScores(key: String, zRangeParams: ZRangeParams): List<Tuple> =
    jedis.zrangeWithScores(key, zRangeParams)

fun Redis.zrangestore(dest: String, src: String, zRangeParams: ZRangeParams): Long =
    jedis.zrangestore(dest, src, zRangeParams)

fun Redis.zrandmember(key: String): String = jedis.zrandmember(key)
fun Redis.zrandmember(key: String, count: Long): List<String> = jedis.zrandmember(key, count)

fun Redis.zrandmemberWithScores(key: String, count: Long): List<Tuple> = jedis.zrandmemberWithScores(key, count)

fun Redis.zcard(key: String): Long = jedis.zcard(key)

fun Redis.zscore(key: String, member: String): Double = jedis.zscore(key, member)

fun Redis.zmscore(key: String, vararg members: String): List<Double> = jedis.zmscore(key, *members)

fun Redis.zpopmax(key: String): Tuple = jedis.zpopmax(key)
fun Redis.zpopmax(key: String, count: Int): List<Tuple> = jedis.zpopmax(key, count)

fun Redis.zpopmin(key: String): Tuple = jedis.zpopmin(key)
fun Redis.zpopmin(key: String, count: Int): List<Tuple> = jedis.zpopmin(key, count)

fun Redis.zcount(key: String, min: String, max: String): Long = jedis.zcount(key, min, max)

fun Redis.zrangeByScore(key: String, min: Double, max: Double): List<String> = jedis.zrangeByScore(key, min, max)
fun Redis.zrangeByScore(key: String, min: String, max: String): List<String> = jedis.zrangeByScore(key, min, max)

fun Redis.zrangeByScore(key: String, min: Double, max: Double, offset: Int, count: Int): List<String> =
    jedis.zrangeByScore(key, min, max, offset, count)

fun Redis.zrangeByScore(key: String, min: String, max: String, offset: Int, count: Int): List<String> =
    jedis.zrangeByScore(key, min, max, offset, count)

fun Redis.zrevrangeByScore(key: String, max: Double, min: Double): List<String> = jedis.zrevrangeByScore(key, max, min)
fun Redis.zrevrangeByScore(key: String, max: String, min: String): List<String> = jedis.zrevrangeByScore(key, max, min)

fun Redis.zrevrangeByScore(key: String, max: Double, min: Double, offset: Int, count: Int): List<String> =
    jedis.zrevrangeByScore(key, max, min, offset, count)

fun Redis.zrevrangeByScore(key: String, max: String, min: String, offset: Int, count: Int): List<String> =
    jedis.zrevrangeByScore(key, max, min, offset, count)

fun Redis.zrangeByScoreWithScores(key: String, min: Double, max: Double): List<Tuple> =
    jedis.zrangeByScoreWithScores(key, min, max)

fun Redis.zrangeByScoreWithScores(key: String, min: String, max: String): List<Tuple> =
    jedis.zrangeByScoreWithScores(key, min, max)

fun Redis.zrevrangeByScoreWithScores(key: String, max: Double, min: Double): List<Tuple> =
    jedis.zrangeByScoreWithScores(key, max, min)

fun Redis.zrevrangeByScoreWithScores(key: String, max: String, min: String): List<Tuple> =
    jedis.zrangeByScoreWithScores(key, max, min)

fun Redis.zrangeByScoreWithScores(key: String, min: Double, max: Double, offset: Int, count: Int): List<Tuple> =
    jedis.zrangeByScoreWithScores(key, min, max, offset, count)

fun Redis.zrangeByScoreWithScores(key: String, min: String, max: String, offset: Int, count: Int): List<Tuple> =
    jedis.zrangeByScoreWithScores(key, min, max, offset, count)

fun Redis.zrevrangeByScoreWithScores(key: String, max: Double, min: Double, offset: Int, count: Int): List<Tuple> =
    jedis.zrangeByScoreWithScores(key, max, min, offset, count)

fun Redis.zrevrangeByScoreWithScores(key: String, max: String, min: String, offset: Int, count: Int): List<Tuple> =
    jedis.zrangeByScoreWithScores(key, max, min, offset, count)

fun Redis.zremrangeByRank(key: String, start: Long, stop: Long): Long = jedis.zremrangeByRank(key, start, stop)

fun Redis.zremrangeByScore(key: String, min: Double, max: Double): Long = jedis.zremrangeByScore(key, min, max)
fun Redis.zremrangeByScore(key: String, min: String, max: String): Long = jedis.zremrangeByScore(key, min, max)

fun Redis.zlexcount(key: String, min: String, max: String): Long = jedis.zlexcount(key, min, max)

fun Redis.zrangeByLex(key: String, min: String, max: String): List<String> = jedis.zrangeByLex(key, min, max)
fun Redis.zrangeByLex(key: String, min: String, max: String, offset: Int, count: Int): List<String> =
    jedis.zrangeByLex(key, min, max, offset, count)

fun Redis.zrevrangeByLex(key: String, max: String, min: String): List<String> = jedis.zrevrangeByLex(key, max, min)
fun Redis.zrevrangeByLex(key: String, max: String, min: String, offset: Int, count: Int): List<String> =
    jedis.zrevrangeByLex(key, max, min, offset, count)

fun Redis.zremrangeByLex(key: String, min: String, max: String): Long = jedis.zremrangeByLex(key, min, max)

// zscan

fun Redis.bzpopmax(timeout: Double, vararg keys: String): KeyValue<String, Tuple> = jedis.bzpopmax(timeout, *keys)

fun Redis.bzpopmin(timeout: Double, vararg keys: String): KeyValue<String, Tuple> = jedis.bzpopmin(timeout, *keys)

fun Redis.zdiff(vararg keys: String): List<String> = jedis.zdiff(*keys)

fun Redis.zdiffWithScores(vararg keys: String): List<Tuple> = jedis.zdiffWithScores(*keys)

fun Redis.zdiffstore(dstKey: String, vararg keys: String): Long = jedis.zdiffstore(dstKey, *keys)

fun Redis.zinter(params: ZParams, vararg keys: String): List<String> = jedis.zinter(params, *keys)

fun Redis.zinterWithScores(params: ZParams, vararg keys: String): List<Tuple> = jedis.zinterWithScores(params, *keys)

fun Redis.zinterstore(dstkey: String, vararg sets: String): Long = jedis.zinterstore(dstkey, *sets)
fun Redis.zinterstore(dstkey: String, params: ZParams, vararg sets: String): Long =
    jedis.zinterstore(dstkey, params, *sets)

fun Redis.zintercard(vararg keys: String): Long = jedis.zintercard(*keys)
fun Redis.zintercard(limit: Long, vararg keys: String): Long = jedis.zintercard(limit, *keys)

fun Redis.zunion(params: ZParams, vararg keys: String): List<String> = jedis.zunion(params, *keys)

fun Redis.zunionWithScores(params: ZParams, vararg keys: String): List<Tuple> = jedis.zunionWithScores(params, *keys)

fun Redis.zunionstore(dstkey: String, vararg sets: String): Long = jedis.zunionstore(dstkey, *sets)
fun Redis.zunionstore(dstkey: String, params: ZParams, vararg sets: String): Long =
    jedis.zunionstore(dstkey, params, *sets)

fun Redis.zmpop(option: SortedSetOption, vararg keys: String): KeyValue<String, List<Tuple>> =
    jedis.zmpop(option, *keys)

fun Redis.zmpop(option: SortedSetOption, count: Int, vararg keys: String): KeyValue<String, List<Tuple>> =
    jedis.zmpop(option, count, *keys)

fun Redis.bzmpop(timeout: Double, option: SortedSetOption, vararg keys: String): KeyValue<String, List<Tuple>> =
    jedis.bzmpop(timeout, option, *keys)

fun Redis.bzmpop(
    timeout: Double,
    option: SortedSetOption,
    count: Int,
    vararg keys: String
): KeyValue<String, List<Tuple>> =
    jedis.bzmpop(timeout, option, count, *keys)