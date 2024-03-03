package icu.twtool.ktor.cloud

import icu.twtool.ktor.cloud.config.core.KtorCloudConfiguration
import io.ktor.http.HttpMethod
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationCallPipeline
import io.ktor.server.application.install
import io.ktor.server.engine.ApplicationEngine
import io.ktor.server.engine.ApplicationEngineFactory
import io.ktor.server.engine.embeddedServer
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.routing.Route
import io.ktor.server.routing.Routing
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
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

        fun <TEngine : ApplicationEngine, TConfiguration : ApplicationEngine.Configuration> start(
            factory: ApplicationEngineFactory<TEngine, TConfiguration>,
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
                    withContext(coroutineContext + element) {
                        proceed()
                    }
                }

                application.configure()
            }.start(wait = true)
        }
    }
}