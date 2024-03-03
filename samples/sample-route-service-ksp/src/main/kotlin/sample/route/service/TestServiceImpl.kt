package sample.route.service

import icu.twtool.ktor.cloud.route.service.annotation.ServiceImpl
import sample.http.TestService

@ServiceImpl
class TestServiceImpl : TestService {

    override suspend fun test(): List<String> {
        return listOf("Test")
    }

    override suspend fun test1(token: String): List<*> {
        return listOf("Test2", token)
    }

    override suspend fun test1(token: String, age: String, name: Map<String, Int>): List<String> {
        return listOf(token, age)
    }
}