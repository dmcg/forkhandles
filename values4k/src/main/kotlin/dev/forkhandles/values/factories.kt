package dev.forkhandles.values

import java.math.BigDecimal
import java.math.BigInteger
import java.net.URL
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.OffsetDateTime
import java.time.OffsetTime
import java.time.Period
import java.time.Year
import java.time.YearMonth
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatter.ISO_LOCAL_DATE
import java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME
import java.time.format.DateTimeFormatter.ISO_LOCAL_TIME
import java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME
import java.time.format.DateTimeFormatter.ISO_OFFSET_TIME
import java.time.format.DateTimeFormatter.ISO_ZONED_DATE_TIME
import java.util.UUID

open class StringValueFactory<DOMAIN>(
    fn: (String) -> DOMAIN, validation: Validation<String> = ::alwaysValid
) : ValueFactory<DOMAIN, String>(fn, { it }, validation)

open class IntValueFactory<DOMAIN>(
    fn: (Int) -> DOMAIN, validation: Validation<Int> = ::alwaysValid
) : ValueFactory<DOMAIN, Int>(fn, String::toInt, validation)

open class LongValueFactory<DOMAIN>(
    fn: (Long) -> DOMAIN, validation: Validation<Long> = ::alwaysValid
) : ValueFactory<DOMAIN, Long>(fn, String::toLong, validation)

open class DoubleValueFactory<DOMAIN>(
    fn: (Double) -> DOMAIN, validation: Validation<Double> = ::alwaysValid
) : ValueFactory<DOMAIN, Double>(fn, String::toDouble, validation)

open class FloatValueFactory<DOMAIN>(
    fn: (Float) -> DOMAIN, validation: Validation<Float> = ::alwaysValid
) : ValueFactory<DOMAIN, Float>(fn, String::toFloat, validation)

open class BooleanValueFactory<DOMAIN>(
    fn: (Boolean) -> DOMAIN, validation: Validation<Boolean> = ::alwaysValid
) : ValueFactory<DOMAIN, Boolean>(fn, String::toBoolean, validation)

open class BigIntegerValueFactory<DOMAIN>(
    fn: (BigInteger) -> DOMAIN, validation: Validation<BigInteger> = ::alwaysValid
) : ValueFactory<DOMAIN, BigInteger>(fn, String::toBigInteger, validation)

open class BigDecimalValueFactory<DOMAIN>(
    fn: (BigDecimal) -> DOMAIN, validation: Validation<BigDecimal> = ::alwaysValid
) : ValueFactory<DOMAIN, BigDecimal>(fn, String::toBigDecimal, validation)

open class UUIDValueFactory<DOMAIN>(
    fn: (UUID) -> DOMAIN, validation: Validation<UUID> = ::alwaysValid
) : ValueFactory<DOMAIN, UUID>(fn, UUID::fromString, validation)

open class URLValueFactory<DOMAIN>(
    fn: (URL) -> DOMAIN, validation: Validation<URL> = ::alwaysValid
) : ValueFactory<DOMAIN, URL>(fn, ::URL, validation)

open class DurationValueFactory<DOMAIN>(
    fn: (Duration) -> DOMAIN, validation: Validation<Duration> = ::alwaysValid
) : ValueFactory<DOMAIN, Duration>(fn, Duration::parse, validation)

open class InstantValueFactory<DOMAIN>(
    fn: (Instant) -> DOMAIN, validation: Validation<Instant> = ::alwaysValid
) : ValueFactory<DOMAIN, Instant>(fn, Instant::parse, validation)

open class LocalDateValueFactory<DOMAIN>(
    fn: (LocalDate) -> DOMAIN, validation: Validation<LocalDate> = ::alwaysValid,
    formatter: DateTimeFormatter = ISO_LOCAL_DATE
) : ValueFactory<DOMAIN, LocalDate>(fn, { LocalDate.parse(it, formatter) }, validation)

open class LocalTimeValueFactory<DOMAIN>(
    fn: (LocalTime) -> DOMAIN,
    validation: Validation<LocalTime> = ::alwaysValid,
    formatter: DateTimeFormatter = ISO_LOCAL_TIME
) : ValueFactory<DOMAIN, LocalTime>(fn, { LocalTime.parse(it, formatter) }, validation)

open class LocalDateTimeValueFactory<DOMAIN>(
    fn: (LocalDateTime) -> DOMAIN,
    validation: Validation<LocalDateTime> = ::alwaysValid,
    formatter: DateTimeFormatter = ISO_LOCAL_DATE_TIME
) : ValueFactory<DOMAIN, LocalDateTime>(fn, { LocalDateTime.parse(it, formatter) }, validation)

open class OffsetDateTimeValueFactory<DOMAIN>(
    fn: (OffsetDateTime) -> DOMAIN,
    validation: Validation<OffsetDateTime> = ::alwaysValid,
    formatter: DateTimeFormatter = ISO_OFFSET_DATE_TIME
) : ValueFactory<DOMAIN, OffsetDateTime>(fn, { OffsetDateTime.parse(it, formatter) }, validation)

open class OffsetTimeValueFactory<DOMAIN>(
    fn: (OffsetTime) -> DOMAIN,
    validation: Validation<OffsetTime> = ::alwaysValid,
    formatter: DateTimeFormatter = ISO_OFFSET_TIME
) : ValueFactory<DOMAIN, OffsetTime>(fn, { OffsetTime.parse(it, formatter) }, validation)

open class PeriodValueFactory<DOMAIN>(
    fn: (Period) -> DOMAIN,
    validation: Validation<Period> = ::alwaysValid
) : ValueFactory<DOMAIN, Period>(fn, Period::parse, validation)

open class YearMonthValueFactory<DOMAIN>(
    fn: (YearMonth) -> DOMAIN,
    validation: Validation<YearMonth> = ::alwaysValid,
    formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM")
) : ValueFactory<DOMAIN, YearMonth>(fn, { YearMonth.parse(it, formatter) }, validation)

open class YearValueFactory<DOMAIN>(
    fn: (Year) -> DOMAIN,
    validation: Validation<Year> = ::alwaysValid,
    formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy")
) : ValueFactory<DOMAIN, Year>(fn, { Year.parse(it, formatter) }, validation)

open class ZonedDateTimeValueFactory<DOMAIN>(
    fn: (ZonedDateTime) -> DOMAIN,
    validation: Validation<ZonedDateTime> = ::alwaysValid,
    formatter: DateTimeFormatter = ISO_ZONED_DATE_TIME
) : ValueFactory<DOMAIN, ZonedDateTime>(fn, { ZonedDateTime.parse(it, formatter) }, validation)
