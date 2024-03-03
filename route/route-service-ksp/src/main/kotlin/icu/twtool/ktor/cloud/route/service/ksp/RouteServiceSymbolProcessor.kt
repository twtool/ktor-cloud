package icu.twtool.ktor.cloud.route.service.ksp

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
import icu.twtool.ksp.ext.enter
import icu.twtool.ksp.ext.plusAssign
import icu.twtool.ksp.ext.tabs
import icu.twtool.ktor.cloud.http.core.annotation.Body
import icu.twtool.ktor.cloud.http.core.annotation.Header
import icu.twtool.ktor.cloud.http.core.annotation.Query
import icu.twtool.ktor.cloud.http.core.annotation.RequestMapping
import icu.twtool.ktor.cloud.route.service.annotation.ServiceImpl
import java.io.OutputStreamWriter

class RouteServiceSymbolProcessor(private val environment: SymbolProcessorEnvironment) : SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val res = mutableListOf<KSAnnotated>()
        resolver.getSymbolsWithAnnotation(ServiceImpl::class.qualifiedName!!, true).forEach {
            if (it !is KSClassDeclaration) res.add(it)
            else handleServiceImplClassDeclaration(it)
        }

        return res
    }

    private fun handleServiceImplClassDeclaration(service: KSClassDeclaration) {
        if (service.classKind != ClassKind.CLASS && service.classKind != ClassKind.OBJECT)
            return warn("$service is not class or object.")
        warn("handle $service ...")

        val superDeclarations = mutableListOf<KSClassDeclaration>()
        service.superTypes.forEach {
            val declaration = it.resolve().declaration
            if (declaration is KSClassDeclaration && declaration.classKind == ClassKind.INTERFACE) {
                superDeclarations.add(declaration)
            }
        }

        val dependencies = Dependencies(true, service.containingFile!!)
        val packageName = service.packageName.asString()
        val className = service.simpleName.asString()
        val fileName = className + "Ext"

        val file = environment.codeGenerator.createNewFile(dependencies, packageName, fileName).writer()

        file += "package $packageName" enter 2
        file += "import icu.twtool.ktor.cloud.KtorCloudApplication" enter 1
        file += "import io.ktor.http.HttpMethod" enter 1
        file += "import io.ktor.server.application.call" enter 1
        file += "import io.ktor.server.request.header" enter 1
        file += "import io.ktor.server.request.receive" enter 1
        file += "import io.ktor.server.response.respond" enter 2
        file += "context(KtorCloudApplication)" enter 1
        file += "fun $className.register() {" enter 2

        superDeclarations.forEach {
            it.getDeclaredFunctions().forEach { func ->
                writeRouteByFunction(func, file)
            }
        }

        file += "}" enter 1

        file.close()
    }

    @OptIn(KspExperimental::class)
    private fun writeRouteByFunction(func: KSFunctionDeclaration, file: OutputStreamWriter) {
        val mapping = func.getAnnotationsByType(RequestMapping::class).firstOrNull()
            ?: return warn("$func not RequestMapping annotation.")

        val funcName = func.simpleName.asString()

        file += 1 tabs "route(\"${mapping.path}\", HttpMethod.${mapping.method}) {" enter 1
        file += 2 tabs "handle {" enter 1
        file += 3 tabs "call.respond($funcName(" enter 1
        func.parameters.forEachIndexed { index, param ->
            val paramName = param.name!!.asString()
            val nullable = if (param.type.resolve().isMarkedNullable) "" else "!!"

            param.getAnnotationsByType(Header::class).firstOrNull()?.let {
                file += 4 tabs "call.request.header(\"${it.name.ifBlank { paramName }}\")$nullable," enter 1
                return@forEachIndexed
            }

            param.getAnnotationsByType(Query::class).firstOrNull()?.let {
                file += 4 tabs "call.request.queryParameters[\"${it.name.ifBlank { paramName }}\"]$nullable," enter 1
                return@forEachIndexed
            }

            param.getAnnotationsByType(Body::class).firstOrNull()?.let {
                file += 4 tabs "call.receive()" enter 1
                return@forEachIndexed
            }
            file += 4 tabs "TODO()," enter 1 // TODO：没有使用注解的参数处理
        }
        file += 3 tabs "))" enter 1
        file += 2 tabs "}" enter 1
        file += 1 tabs "}" enter 2
    }

    private fun warn(msg: String) {
        environment.logger.warn(msg)
    }
}