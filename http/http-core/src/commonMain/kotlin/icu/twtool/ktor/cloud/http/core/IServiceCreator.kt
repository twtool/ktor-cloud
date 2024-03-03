package icu.twtool.ktor.cloud.http.core

interface IServiceCreator {

    suspend  fun <T> request(request: KtorCloudRequest): T
}