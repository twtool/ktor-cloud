package icu.twtool.ktor.cloud.http.ksp

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.getAnnotationsByType
import com.google.devtools.ksp.getDeclaredFunctions
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSValueParameter
import icu.twtool.ksp.ext.enter
import icu.twtool.ksp.ext.parse
import icu.twtool.ksp.ext.plusAssign
import icu.twtool.ksp.ext.tabs
import icu.twtool.ktor.cloud.http.core.IServiceCreator
import icu.twtool.ktor.cloud.http.core.KtorCloudRequest
import icu.twtool.ktor.cloud.http.core.annotation.Body
import icu.twtool.ktor.cloud.http.core.annotation.Header
import icu.twtool.ktor.cloud.http.core.annotation.Query
import icu.twtool.ktor.cloud.http.core.annotation.RequestMapping
import icu.twtool.ktor.cloud.http.core.annotation.Service
import java.io.OutputStreamWriter

class HttpCoreSymbolProcessor(private val environment: SymbolProcessorEnvironment) : SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val res = mutableListOf<KSAnnotated>()
        resolver.getSymbolsWithAnnotation(Service::class.qualifiedName!!).forEach {
            if (it !is KSClassDeclaration) res.add(it)
            else handleServiceClassDeclaration(it)
        }
        return res
    }

    @OptIn(KspExperimental::class)
    private fun handleServiceClassDeclaration(service: KSClassDeclaration) {
        if (service.classKind != ClassKind.INTERFACE) return warn("$service is not interface.")
        warn("handle $service ...")

        val dependencies = Dependencies(true, service.containingFile!!)
        val packageName = service.packageName.asString()
        val className = service.simpleName.asString()
        val implClassName = className + "Impl"
        val file = environment.codeGenerator.createNewFile(dependencies, packageName, implClassName).writer()

        file += "package $packageName" enter 2
        file += "import ${IServiceCreator::class.qualifiedName}" enter 1
        file += "import ${KtorCloudRequest::class.qualifiedName}" enter 1
        file += "import io.ktor.http.HttpMethod" enter 2
        file += "import io.ktor.util.reflect.typeInfo" enter 2

        file += "class $implClassName(private val creator: ${IServiceCreator::class.simpleName}): $className {" enter 2

        val serviceAnno = service.getAnnotationsByType(Service::class).first()
        service.getDeclaredFunctions().forEach {
            writeFunctionImpl(serviceAnno, it, file)
        }

        file += 1 tabs "override fun toString(): String = \"${service.qualifiedName?.asString()}\"" enter 2

        file += "}" enter 1

        if (service.declarations.any { it is KSClassDeclaration && it.isCompanionObject }) {
            file += "" enter 1

            file += "actual fun $className.Companion.create(creator: ${IServiceCreator::class.simpleName}): $className = $implClassName(creator)"
        }

        file.close()
    }

    @OptIn(KspExperimental::class)
    private fun writeFunctionImpl(service: Service, function: KSFunctionDeclaration, file: OutputStreamWriter) {
        val mapping = function.getAnnotationsByType(RequestMapping::class).first()
        val parameters = function.parameters.parse(environment.logger)
        val returnType = function.returnType?.parse(environment.logger) ?: "Unit"

        val headers = mutableMapOf<String, String>()
        val queries = mutableMapOf<String, String>()
        var body: KSValueParameter? = null

        function.parameters.forEach { param ->
            param.getAnnotationsByType(Header::class).firstOrNull()?.let {
                val name = param.name?.asString()!!
                headers[it.name.ifBlank { name }] = name
                return@forEach
            }
            param.getAnnotationsByType(Query::class).firstOrNull()?.let {
                val name = param.name?.asString()!!
                queries[it.name.ifBlank { name }] = name
                return@forEach
            }
            param.getAnnotationsByType(Body::class).firstOrNull()?.let {
                body = param
                return@forEach
            }
        }

        file += 1 tabs "override suspend fun ${function.simpleName.asString()}($parameters): $returnType {" enter 1
        file += 2 tabs "val request = ${KtorCloudRequest::class.simpleName}(" enter 1
        file += 3 tabs "serviceName = \"${service.name}\"," enter 1
        file += 3 tabs "servicePath = \"${service.path}\"," enter 1
        file += 3 tabs "method = HttpMethod.${mapping.method}," enter 1
        file += 3 tabs "path = \"${mapping.path}\"," enter 1
        file += 3 tabs "headers = mapOf(" enter 1
        headers.forEach { (headerName, paramName) ->
            file += 4 tabs "\"$headerName\" to $paramName" enter 1
        }
        file += 3 tabs ")," enter 1
        file += 3 tabs "queries = mapOf(" enter 1
        queries.forEach { (queryName, paramName) ->
            file += 4 tabs "\"$queryName\" to $paramName" enter 1
        }
        file += 3 tabs ")," enter 1
        body?.let {
            file += 3 tabs "body = typeInfo<${it.type.parse(environment.logger)}>() to ${it.name?.asString()}," enter 1
        }
        file += 3 tabs "returnType = typeInfo<${returnType}>()," enter 1
        file += 2 tabs ")" enter 1
        file += 2 tabs "return creator.request(request)" enter 1
        file += 1 tabs "}" enter 2
    }

    private fun warn(msg: String) {
        environment.logger.warn(msg)
    }
}