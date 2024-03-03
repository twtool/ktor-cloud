package icu.twtool.ksp.ext

import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSTypeReference


fun KSTypeReference.parse(logger: KSPLogger): String {
    val builder = StringBuilder()
    parse(builder, logger)
    return builder.toString()
}

fun KSTypeReference.parse(builder: StringBuilder, logger: KSPLogger) {
    builder.append(resolve().declaration.qualifiedName?.asString())
    element?.typeArguments?.parse(builder, logger)
}