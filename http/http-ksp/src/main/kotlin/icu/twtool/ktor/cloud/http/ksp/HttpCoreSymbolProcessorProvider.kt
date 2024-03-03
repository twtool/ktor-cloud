package icu.twtool.ktor.cloud.http.ksp

import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider

class HttpCoreSymbolProcessorProvider : SymbolProcessorProvider{

    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return HttpCoreSymbolProcessor(environment)
    }
}