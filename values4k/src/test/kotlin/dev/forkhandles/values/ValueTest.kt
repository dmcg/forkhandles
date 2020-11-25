package dev.forkhandles.values

import com.natpryce.hamkrest.MatchResult.Match
import com.natpryce.hamkrest.MatchResult.Mismatch
import com.natpryce.hamkrest.Matcher
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import dev.forkhandles.values.Maskers.hidden
import dev.forkhandles.values.Maskers.obfuscated
import dev.forkhandles.values.Maskers.substring
import org.junit.jupiter.api.Test

class NotEmptyString private constructor(value: String) : Value<String>(value) {
    override fun toString() = masking(value)
    companion object : StringValueFactory<NotEmptyString>(::NotEmptyString, String::isNotEmpty)
}

class NotNegativeInt private constructor(value: Int) : Value<Int>(value) {
    companion object : IntValueFactory<NotNegativeInt>(::NotNegativeInt, { it > 0 })
}

class HiddenValue private constructor(value: String) : Value<String>(value) {
    override fun toString() = masking(value)
    companion object : StringValueFactory<HiddenValue>(::HiddenValue, masking = hidden('t'))
}

class ObsfucatedValue private constructor(value: String) : Value<String>(value) {
    override fun toString() = masking(value)
    companion object : StringValueFactory<ObsfucatedValue>(::ObsfucatedValue, masking = obfuscated())
}

class SubstringValue private constructor(value: String) : Value<String>(value) {
    override fun toString() = masking(value)
    companion object : StringValueFactory<SubstringValue>(::SubstringValue, masking = substring(3, 5))
}

class ValueTest {
    @Test
    fun `toString value`() {
        assertThat(NotEmptyString.of("hellohello").toString(), equalTo("hellohello"))
        assertThat(HiddenValue.of("hellohello").toString(), equalTo("tttttttttt"))
        assertThat(SubstringValue.of("hellohello").toString(), equalTo("hel**ello"))
        assertThat(ObsfucatedValue.of("hello").toString(), object : Matcher<String> {
            override fun invoke(actual: String) =
                if (actual.all { it == '*' }) Match else Mismatch(actual)

            override val description: String = "all *"
        })
    }

    @Test
    fun `hashcode value`() {
        assertThat(NotEmptyString.of("hello").hashCode(), equalTo("hello".hashCode()))
    }

    @Test
    fun equality() {
        val myValue = NotEmptyString.of("hello")
        assertThat(myValue == myValue, equalTo(true))
        assertThat(myValue == NotEmptyString.of("hello"), equalTo(true))
        assertThat(myValue == NotEmptyString.of("hello2"), equalTo(false))
    }

}
