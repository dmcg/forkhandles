package dev.forkhandles.values

import dev.forkhandles.result4k.*

/**
 * Base value type for inline classes which enables type-safe primitives, along with Validation.
 */
abstract class ValueFactory<DOMAIN, PRIMITIVE>(
    internal val coerceFn: (PRIMITIVE) -> DOMAIN,
    private val validation: Validation<PRIMITIVE>? = null,
    internal val parseFn: (String) -> PRIMITIVE
) {
    fun parse(string: String): DOMAIN? = parseToResult(string).valueOrNull()

    fun of(value: PRIMITIVE): DOMAIN = resultOf(value).recover {
        throw IllegalArgumentException(it)
    }

    fun resultOf(value: PRIMITIVE): Result<DOMAIN, String> {
        val errorMessage = (validation ?: { true }).errorMessageOrNull(value)
        return when (errorMessage) {
            null -> Success(coerceFn(value))
            else -> Failure(errorMessage)
        }
    }

    fun parseToResult(string: String): Result<DOMAIN, String> =
        try {
            resultOf(parseFn(string))
        } catch (x: Exception) {
            Failure(x.message ?: x.javaClass.simpleName)
        }
}

/**
 * Return a Object/null based on validation.
 */
fun <DOMAIN, PRIMITIVE> ValueFactory<DOMAIN, PRIMITIVE>.ofOrNull(value: PRIMITIVE): DOMAIN? = resultOf(value).valueOrNull()
