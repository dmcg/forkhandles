@file:Suppress("EXPERIMENTAL_FEATURE_WARNING")

import dev.forkhandles.result4k.recover
import dev.forkhandles.result4k.resultFrom
import dev.forkhandles.values.IntValueFactory
import dev.forkhandles.values.Maskers.hidden
import dev.forkhandles.values.StringValue
import dev.forkhandles.values.StringValueFactory
import dev.forkhandles.values.Value
import dev.forkhandles.values.minValue
import dev.forkhandles.values.regex

inline class Money(val value: Int) {
    override fun toString() = masking(value)
    companion object : IntValueFactory<Money>(::Money, 1.minValue)
}

class SortCode private constructor(value: String) : StringValue(value) {
    override fun toString() = masking(value)
    companion object : StringValueFactory<SortCode>(::SortCode, "\\d{6}".regex)
}

class AccountNumber private constructor(value: String) : Value<String>(value) {
    override fun toString() = masking(value)
    companion object : StringValueFactory<AccountNumber>(::AccountNumber, "\\d{8}".regex, hidden())
}

fun main() {
    printOrError { Money.of(1) } // ok
    printOrError { Money.of(0) } // will throw IllegalArgumentException
    printOrError { Money.parse("not money") } // will return null

    printOrError { SortCode.of("123qwe") } // will throw IllegalArgumentException
    printOrError { AccountNumber.of("12345678") } // ok
    printOrError { SortCode.parse("123456") } // ok
    printOrError { SortCode.of("123qwe") } // will throw IllegalArgumentException
    printOrError { AccountNumber.parse("1234567") } // will return null
}

private fun printOrError(fn: () -> Any?) = println(resultFrom(fn).recover { it.message })
