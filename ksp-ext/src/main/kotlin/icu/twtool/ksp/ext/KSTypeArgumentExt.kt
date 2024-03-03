package icu.twtool.ksp.ext

import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSTypeArgument
import com.google.devtools.ksp.symbol.Nullability
import com.google.devtools.ksp.symbol.Variance

fun List<KSTypeArgument>.parse(acc: StringBuilder, logger: KSPLogger) {
    if (isEmpty()) return
    if (isNotEmpty()) {
        acc.append('<')
        forEachIndexed { index, type ->
            if (index != 0) acc.append(", ")
            type.parse(acc, logger)
        }
        acc.append('>')
    }
}


fun KSTypeArgument.parse(acc: StringBuilder, logger: KSPLogger) {
    when (val variance: Variance = variance) {
        Variance.STAR -> { // <*>
            acc.append('*')
            return
        }

        Variance.COVARIANT, Variance.CONTRAVARIANT -> acc.append("${variance.label} ") // <out ...>, <in ...>
        Variance.INVARIANT -> {} /*Do nothing.*/
    }
    val resolvedType = type?.resolve()
    acc.append(resolvedType?.declaration?.qualifiedName?.asString() ?: run {
        logger.error("Invalid type argument", this)
    })
    // Generating nested generic parameters if any.
    val genericArguments = type?.element?.typeArguments ?: emptyList()
    genericArguments.parse(acc, logger)
    // Handling nullability.
    acc.append(if (resolvedType?.nullability == Nullability.NULLABLE) "?" else "")
}