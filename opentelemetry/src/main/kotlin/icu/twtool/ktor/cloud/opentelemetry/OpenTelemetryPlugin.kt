package icu.twtool.ktor.cloud.opentelemetry

import icu.twtool.ktor.cloud.KtorCloudApplication
import icu.twtool.ktor.cloud.Plugin
import icu.twtool.ktor.cloud.discovery.core.HttpClient
import icu.twtool.ktor.cloud.discovery.core.ServiceName
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.plugin
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.server.application.ApplicationCallPipeline
import io.ktor.server.application.call
import io.ktor.server.request.ApplicationRequest
import io.ktor.server.request.header
import io.ktor.server.request.path
import io.ktor.util.AttributeKey
import io.ktor.util.flattenEntries
import io.opentelemetry.api.OpenTelemetry
import io.opentelemetry.api.baggage.propagation.W3CBaggagePropagator
import io.opentelemetry.api.trace.SpanKind
import io.opentelemetry.api.trace.Tracer
import io.opentelemetry.api.trace.propagation.W3CTraceContextPropagator
import io.opentelemetry.context.Context
import io.opentelemetry.context.propagation.ContextPropagators
import io.opentelemetry.context.propagation.TextMapGetter
import io.opentelemetry.context.propagation.TextMapPropagator
import io.opentelemetry.context.propagation.TextMapSetter
import io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter
import io.opentelemetry.sdk.OpenTelemetrySdk
import io.opentelemetry.sdk.resources.Resource
import io.opentelemetry.sdk.trace.SdkTracerProvider
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor

private val OpenTelemetryPluginKey = AttributeKey<OpenTelemetryPlugin>("icu.twtool.ktor.cloud.opentelemetry")

class OpenTelemetryPlugin : Plugin {

    private var opentelemetry: OpenTelemetry = OpenTelemetry.noop()

    fun getTracer(scopeName: String): Tracer = opentelemetry.getTracer(scopeName)

    override fun KtorCloudApplication.install() {
        val resource: Resource = Resource.getDefault().toBuilder().put("service.name", config[ServiceName]).build()

        val sdkTracerProvider: SdkTracerProvider = SdkTracerProvider.builder()
            .addSpanProcessor(
                BatchSpanProcessor.builder(
                    OtlpGrpcSpanExporter.builder()
                        .setEndpoint(config[OpenTelemetryEndpointKey])
                        .build()
                ).build()

            ).setResource(resource)
            .build()

//        val sdkMeterProvider: SdkMeterProvider = SdkMeterProvider.builder()
//            .registerMetricReader(PeriodicMetricReader.builder(LoggingMetricExporter.create()).build())
//            .setResource(resource)
//            .build()
//
//        val sdkLoggerProvider: SdkLoggerProvider = SdkLoggerProvider.builder()
//            .addLogRecordProcessor(BatchLogRecordProcessor.builder(SystemOutLogRecordExporter.create()).build())
//            .setResource(resource)
//            .build()

        opentelemetry = OpenTelemetrySdk.builder()
            .setTracerProvider(sdkTracerProvider)
//            .setMeterProvider(sdkMeterProvider)
//            .setLoggerProvider(sdkLoggerProvider)
            .setPropagators(
                ContextPropagators.create(
                    TextMapPropagator.composite(
                        W3CTraceContextPropagator.getInstance(),
                        W3CBaggagePropagator.getInstance()
                    )
                )
            )
            .build()

        application.attributes.put(OpenTelemetryPluginKey, this@OpenTelemetryPlugin)

        val tracer = opentelemetry.getTracer("OpenTelemetryPlugin")

        application.intercept(ApplicationCallPipeline.Plugins) {
            val parentSpan =
                opentelemetry.propagators.textMapPropagator.extract(Context.current(), call.request, requestGetter)

            val span = tracer.spanBuilder(call.request.path())
                .setParent(parentSpan)
                .startSpan()
            try {
                span.makeCurrent().use {
                    proceed()
                }
            } catch (e: Exception) {
                span.recordException(e)
                throw e
            } finally {
                span.end()
            }
        }

        HttpClient.plugin(HttpSend).intercept { request ->
            opentelemetry.propagators.textMapPropagator.inject(Context.current(), request, requestSetter)
            execute(request)
        }
    }

    companion object {

        context(KtorCloudApplication)
        fun install() {
            install(OpenTelemetryPlugin())
        }

        context(KtorCloudApplication)
        fun getInstance(): OpenTelemetryPlugin =
            application.attributes.getOrNull(OpenTelemetryPluginKey) ?: OpenTelemetryPlugin()
    }
}

private val requestSetter = TextMapSetter<HttpRequestBuilder> { carrier, key, value ->
    carrier?.headers?.append(key, value)
}
private val requestGetter = object : TextMapGetter<ApplicationRequest> {
    override fun keys(request: ApplicationRequest): MutableIterable<String> {
        return request.headers.flattenEntries().map { it.first }.toMutableSet()
    }

    override fun get(request: ApplicationRequest?, key: String): String? {
        return request?.header(key)
    }
}