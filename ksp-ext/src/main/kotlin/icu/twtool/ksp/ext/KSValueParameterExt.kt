package icu.twtool.ksp.ext

import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSValueParameter

fun List<KSValueParameter>.parse(logger: KSPLogger): String {
    return buildString { parse(this, logger) }
}

fun List<KSValueParameter>.parse(builder: StringBuilder, logger: KSPLogger) {
    forEachIndexed { index, param ->
        if (index != 0) builder.append(", ")
        param.parse(builder, logger)
    }
}

fun KSValueParameter.parse(builder: StringBuilder, logger: KSPLogger) {
    builder.append(name?.asString()).append(':').append(' ')
        .append(type.resolve().declaration.qualifiedName?.asString())
    type.element?.typeArguments?.parse(builder, logger)
}