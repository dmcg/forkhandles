package dev.forkhandles.values

import com.natpryce.hamkrest.absent
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.throws
import dev.forkhandles.result4k.Failure
import dev.forkhandles.result4k.Success
import org.junit.jupiter.api.Test

class ValueFactoryTest {

    @Test
    fun `throwable factory`() {
        assertThat(NotNegativeInt.of(123), equalTo(NotNegativeInt.of(123)))
        assertThat({ NotNegativeInt.of(0) }, throws<IllegalArgumentException>())
    }

    @Test
    fun `nullable factory`() {
        assertThat(NotEmptyString.ofOrNull("hello"), equalTo(NotEmptyString.of("hello")))
        assertThat(NotEmptyString.ofOrNull(""), absent())
    }

    @Test
    fun `result factory`() {
        assertThat(NotEmptyString.resultOf("hello"), equalTo(Success(NotEmptyString.of("hello"))))
        assertThat(NotEmptyString.resultOf(""), equalTo(Failure("Validation failed for: (\"\")")))
    }

    @Test
    fun `throwable parse`() {
        assertThat(NotNegativeInt.parse("123"), equalTo(NotNegativeInt.of(123)))
        assertThat(NotNegativeInt.parse(""), absent())
    }

    @Test
    fun `result parse`() {
        assertThat(NotNegativeInt.parseToResult("123"), equalTo(Success(NotNegativeInt.of(123))))
        assertThat(NotEmptyString.parseToResult(""), equalTo(Failure("Validation failed for: (\"\")")))
    }
}
