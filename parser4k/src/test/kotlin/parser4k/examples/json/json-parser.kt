package parser4k.examples.json

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import parser4k.InOrderExtensions
import parser4k.OneOf
import parser4k.OneOfExtensions
import parser4k.Parser
import parser4k.commonparsers.joinedWith
import parser4k.examples.json.JsonParser.parse
import parser4k.except
import parser4k.joinToString
import parser4k.map
import parser4k.oneOf
import parser4k.oneOrMore
import parser4k.optional
import parser4k.parseWith
import parser4k.ref
import parser4k.shouldEqual
import parser4k.skipFirst
import parser4k.skipWrapper
import parser4k.str
import parser4k.zeroOrMore
import java.io.File

/**
 * Based on https://www.json.org/json-en.html
 */
private object JsonParser : OneOfExtensions, InOrderExtensions {

    fun parse(s: String) = s.parseWith(json)

    private val ws = zeroOrMore('\u0020' or '\u000A' or '\u000D' or '\u0009')
    private val sign: OneOf<String> = "+" or "-" or ""
    private val oneNine = '1'..'9'
    private val digit = '0' or oneNine
    private val digits = oneOrMore(digit).map { it.joinToString("") }
    private val exponent = optional(('E' or 'e') + sign + digits).map { it?.joinToString() ?: "" }
    private val fraction = optional('.' + digits).map { it?.joinToString() ?: "" }
    private val integer =
        (sign + oneNine + digits).map { it.joinToString() } or
            (sign + digit).map { it.joinToString() } or
            (oneNine + digits).map { it.joinToString() } or
            digit.map { it.toString() }

    private val number = (integer + fraction + exponent)
        .map { (integer, fraction, exponent) ->
            if (fraction.isEmpty() && exponent.isEmpty()) integer.toInt()
            else (integer + fraction + exponent).toDouble()
        }

    private val hex = digit or 'A'..'F' or 'a'..'f'
    private val escape: OneOf<Char> =
        oneOf('"', '\\', '/', 'b', 'f', 'n', 'r', 't').map { it.toEscapedChar() } or
            ('u' + hex + hex + hex + hex).skipFirst().map { it.joinToString().toInt(16).toChar() }

    private val characters = zeroOrMore(
        (0x0020.toChar()..0x10FFFF.toChar()).except('"', '\\') or
            ('\\' + escape).skipFirst()
    )
    private val string = ('"' + characters + '"')
        .skipWrapper().map { it.joinToString("") }

    private val member: Parser<Pair<Any, Any?>> =
        (ws + string + ws + str(":") + ref { element })
            .map { (_, id, _, _, element) -> Pair(id, element) }

    private val obj =
        ('{' + ws + '}').map { emptyMap<Any, Any>() } or
            ('{' + member.joinedWith(",") + '}').skipWrapper().map { it.toMap() }

    private val array =
        ('[' + ws + ']').map { emptyList<Any>() } or
            ('[' + ref { element }.joinedWith(",") + ']').skipWrapper()

    private val `true` = str("true").map { true }
    private val `false` = str("false").map { false }
    private val `null` = str("null").map { null }

    private val value: Parser<Any?> = (
        obj or
            array or
            string or
            number or
            `true` or
            `false` or
            `null`
        )

    private val element = (ws + value + ws).skipWrapper()

    private val json = element

    private fun Char.toEscapedChar() =
        when (this) {
            'b' -> '\b'
            'f' -> '\u000c'
            'n' -> '\n'
            'r' -> '\r'
            't' -> '\t'
            else -> this
        }
}


class JsonParserTests {
    private val emptyObject = emptyMap<String, Any>()
    private val emptyList = emptyList<Any>()

    @Test
    fun `null`() {
        parse("null") shouldEqual null
    }

    @Test
    fun booleans() {
        parse("true") shouldEqual true
        parse("false") shouldEqual false
    }

    @Test
    fun integers() {
        parse("0") shouldEqual 0
        parse("+1") shouldEqual 1
        parse("-1") shouldEqual -1
        parse("123") shouldEqual 123
        parse("+123") shouldEqual 123
        parse("-123") shouldEqual -123
        parse("1234567890") shouldEqual 1234567890
    }

    @Test
    fun `floating points`() {
        parse("123.456") shouldEqual 123.456
        parse("+123.456") shouldEqual 123.456
        parse("-123.456") shouldEqual -123.456
        parse("-123.456") shouldEqual -123.456
        parse("-9876.543210") shouldEqual -9876.543210
        parse("1e1") shouldEqual 1e1
        parse("1e-1") shouldEqual 1e-1
        parse("1e00") shouldEqual 1e00
        parse("0.1e1") shouldEqual 0.1e1
        parse("2e+00") shouldEqual 2e+00
        parse("2e-00") shouldEqual 2e-00
        parse("0.123456789e-12") shouldEqual 0.123456789e-12
        parse("1.234567890E+34") shouldEqual 1.234567890E+34
        parse("23456789012E66") shouldEqual 23456789012E66
    }

    @Test
    fun strings() {
        fun String.quoted() = "\"$this\""

        parse("".quoted()) shouldEqual ""
        parse(" ".quoted()) shouldEqual " "
        parse("""\"""".quoted()) shouldEqual "\""
        parse("""\\""".quoted()) shouldEqual "\\"
        parse("""\b""".quoted()) shouldEqual "\b"
        parse("""\f""".quoted()) shouldEqual "\u000c"
        parse("""\n""".quoted()) shouldEqual "\n"
        parse("""\r""".quoted()) shouldEqual "\r"
        parse("""\t""".quoted()) shouldEqual "\t"
        parse("""\u0123\u4567\u89AB\uCDEF\uabcd\uef4A""".quoted()) shouldEqual "\u0123\u4567\u89AB\uCDEF\uabcd\uef4A"
        parse("abcdefghijklmnopqrstuvwyz".quoted()) shouldEqual "abcdefghijklmnopqrstuvwyz"
        parse("ABCDEFGHIJKLMNOPQRSTUVWYZ".quoted()) shouldEqual "ABCDEFGHIJKLMNOPQRSTUVWYZ"
        parse("0123456789".quoted()) shouldEqual "0123456789"
        parse("`1~!@#$%^&*()_+-={':[,]}|;.</>?".quoted()) shouldEqual "`1~!@#$%^&*()_+-={':[,]}|;.</>?"
    }

    @Test
    fun arrays() {
        parse("[]") shouldEqual emptyList
        parse("[1, 2, 3]") shouldEqual listOf(1, 2, 3)
        parse("[[1, 2, 3], [4, 5, [6]]]") shouldEqual listOf(listOf(1, 2, 3), listOf(4, 5, listOf(6)))
        parse("""[1,2 , 3
            |
            |,
            |
            |4 , 5        ,          6           ,7        ]
            |""".trimMargin()) shouldEqual listOf(1, 2, 3, 4, 5, 6, 7)
    }

    @Test
    fun objects() {
        parse("""{}""") shouldEqual emptyObject
        parse("""{ "foo": 123 }""") shouldEqual mapOf("foo" to 123)
        parse("""{ "foo": 123, "bar": "woof" }""") shouldEqual mapOf("foo" to 123, "bar" to "woof")
        parse("""{ "foo": { "bar": 123 }}""") shouldEqual mapOf("foo" to mapOf("bar" to 123))
        parse("""{ "foo": [1,2,3] }""") shouldEqual mapOf("foo" to listOf(1, 2, 3))
        parse("""[{ "foo": 123 }]""") shouldEqual listOf(mapOf("foo" to 123))
        parse("""{"jsontext": "{\"object with 1 member\":[\"array with 1 element\"]}"}""") shouldEqual
            mapOf("jsontext" to """{"object with 1 member":["array with 1 element"]}""")
    }

    @Test
    fun `test cases from json-dot-org`() {
        // These tests cases were downloaded from https://www.json.org/JSON_checker
        // excluding fail1.json because strings are valid root elements according to json grammar,
        // excluding fail18.json because there is no official maximum depth for nested arrays.
        val testCases = File("src/test/kotlin/parser4k/examples/json/testcases").listFiles()
            ?: fail("Couldn't find files with json test cases")

        testCases.forEach { file ->
            try {
                parse(file.readText())
                if (file.name.startsWith("fail")) fail("Expected failure: ${file.name}")
            } catch (e: Exception) {
                if (file.name.startsWith("pass")) throw e
            }
        }
    }
}
