package sample.http

import icu.twtool.ktor.cloud.http.core.HttpMethod
import icu.twtool.ktor.cloud.http.core.annotation.Body
import icu.twtool.ktor.cloud.http.core.annotation.Header
import icu.twtool.ktor.cloud.http.core.annotation.Query
import icu.twtool.ktor.cloud.http.core.annotation.RequestMapping
import icu.twtool.ktor.cloud.http.core.annotation.Service

@Service("TestService", "test")
interface TestService {

    @RequestMapping(HttpMethod.Post, "test1")
    suspend fun test(): List<String>

    @RequestMapping(HttpMethod.Post, "test2")
    suspend fun test1(token: String): List<*>

    @RequestMapping(HttpMethod.Post, "test3")
    suspend fun test1(@Header token: String, @Query age: String, @Body name: Map<String, Int>): List<String>

    companion object
}