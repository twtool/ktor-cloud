package icu.twtool.ktor.cloud

import icu.twtool.ktor.cloud.config.core.KtorCloudConfiguration
import io.ktor.http.HttpMethod
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationCallPipeline
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.engine.ApplicationEngine
import io.ktor.server.engine.ApplicationEngineFactory
import io.ktor.server.engine.embeddedServer
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.Routing
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import io.ktor.util.reflect.TypeInfo
import io.ktor.util.reflect.typeInfo
import kotlinx.coroutines.withContext
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext

class ApplicationElement(val value: KtorCloudApplication) : AbstractCoroutineContextElement(ApplicationElement) {
    companion object Key : CoroutineContext.Key<ApplicationElement>
}

suspend fun applicationOr(): KtorCloudApplication = coroutineContext[ApplicationElement]!!.value

class KtorCloudApplication private constructor(
    val application: Application,
    val config: KtorCloudConfiguration,
    val routing: Routing
) {

    fun route(path: String, method: HttpMethod, build: Route.() -> Unit) {
        routing.route(path, method, build)
    }

    fun install(plugin: Plugin) {
        with(plugin) {
            install()
        }
    }

    companion object {
        inline fun <TEngine : ApplicationEngine, TConfiguration : ApplicationEngine.Configuration, reified TExceptionHandlerRes : Any> start(
            factory: ApplicationEngineFactory<TEngine, TConfiguration>,
            noinline exceptionHandler: ((Throwable) -> TExceptionHandlerRes),
            noinline configure: KtorCloudApplication.() -> Unit
        ) = start(factory, exceptionHandler, typeInfo<TExceptionHandlerRes>(), configure)

        fun <TEngine : ApplicationEngine, TConfiguration : ApplicationEngine.Configuration> start(
            factory: ApplicationEngineFactory<TEngine, TConfiguration>,
            configure: KtorCloudApplication.() -> Unit
        ) = start<TEngine, TConfiguration, Any>(factory, null, null, configure)

        fun <TEngine : ApplicationEngine, TConfiguration : ApplicationEngine.Configuration, TExceptionHandlerRes : Any> start(
            factory: ApplicationEngineFactory<TEngine, TConfiguration>,
            exceptionHandler: ((Throwable) -> TExceptionHandlerRes)? = null,
            exceptionHandlerResTypeInfo: TypeInfo? = null,
            configure: KtorCloudApplication.() -> Unit
        ) {
            val configuration = KtorCloudConfiguration()
            configuration.load()
            embeddedServer(factory, configuration[PortKey], configuration[HostKey]) {
                install(ContentNegotiation) {
                    json()
                }

                val application = KtorCloudApplication(
                    this@embeddedServer,
                    configuration,
                    routing {}
                )

                val element = ApplicationElement(application)

                intercept(ApplicationCallPipeline.Plugins) {
                    try {
                        withContext(coroutineContext + element) {
                            proceed()
                        }
                    } catch (e: Throwable) {
                        if (exceptionHandler != null && exceptionHandlerResTypeInfo != null) {
                            call.respond(exceptionHandler(e), exceptionHandlerResTypeInfo)
                        } else throw e
                    }
                }

                application.configure()
            }.start(wait = true)
        }
    }
}