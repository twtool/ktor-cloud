package icu.twtool.ktor.cloud.exposed

import icu.twtool.ktor.cloud.KtorCloudApplication
import icu.twtool.ktor.cloud.applicationOr
import io.ktor.util.AttributeKey
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.transactions.transactionManager

private val DatabaseKey = AttributeKey<Database>("icu.twtool.ktor.cloud.exposed.key")

val KtorCloudApplication.database: Database get() = application.attributes[DatabaseKey]

fun KtorCloudApplication.initExposed(statement: Transaction.() -> Unit = {}) {
    val database = Database.connect(config[UrlKey], config[DriverKey], config[UserKey], config[PasswordKey])
    application.attributes.put(
        DatabaseKey,
        database
    )
    transaction(database, statement)
}

suspend fun <T> transaction(
    transactionIsolation: Int? = null,
    readOnly: Boolean? = null,
    statement: Transaction.() -> T
): T {
    val db = applicationOr().application.attributes[DatabaseKey]
    return transaction(
        transactionIsolation ?: db.transactionManager.defaultIsolationLevel,
        readOnly ?: db.transactionManager.defaultReadOnly,
        db,
        statement
    )
}

fun <T> Database.transaction(
    transactionIsolation: Int? = null,
    readOnly: Boolean? = null,
    statement: Transaction.() -> T
): T {
    return transaction(
        transactionIsolation ?: transactionManager.defaultIsolationLevel,
        readOnly ?: transactionManager.defaultReadOnly,
        this,
        statement,
    )
}