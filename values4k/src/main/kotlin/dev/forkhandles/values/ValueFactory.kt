package dev.forkhandles.values

import dev.forkhandles.result4k.*

/**
 * Base value type for inline classes which enables type-safe primitives, along with Validation.
 */
abstract class ValueFactory<DOMAIN, PRIMITIVE: Any>(
    internal val coerceFn: (PRIMITIVE) -> DOMAIN,
    internal val parseFn: (String) -> PRIMITIVE,
    private val validation: Validation<PRIMITIVE> = ::alwaysValid,
    val masking: Masking<PRIMITIVE> = Maskers.public
) {
    fun resultOf(value: PRIMITIVE): Result<DOMAIN, String> =
        validation.errorMessageOrNull(value)?.let { errorMessage ->
            Failure(errorMessage)
        } ?: Success(coerceFn(value))

    fun of(value: PRIMITIVE): DOMAIN = resultOf(value).recover {
        throw IllegalArgumentException(it)
    }

    fun parseToResult(string: String): Result<DOMAIN, String> =
        try {
            resultOf(parseFn(string))
        } catch (x: Exception) {
            Failure(x.message ?: x.javaClass.simpleName)
        }

    fun parse(string: String): DOMAIN? = parseToResult(string).valueOrNull()
}

/**
 * Return a Object/null based on validation.
 */
fun <DOMAIN, PRIMITIVE: Any> ValueFactory<DOMAIN, PRIMITIVE>.ofOrNull(value: PRIMITIVE): DOMAIN? =
    resultOf(value).valueOrNull()
