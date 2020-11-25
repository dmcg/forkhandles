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
    fn: (String) -> DOMAIN,
    validation: Validation<String> = ::alwaysValid,
    masking: Masking<String> = Maskers.public
) : ValueFactory<DOMAIN, String>(fn, { it }, validation, masking)

open class IntValueFactory<DOMAIN>(
    fn: (Int) -> DOMAIN,
    validation: Validation<Int> = ::alwaysValid,
    masking: Masking<Int> = Maskers.public
) : ValueFactory<DOMAIN, Int>(fn, String::toInt, validation, masking)

open class LongValueFactory<DOMAIN>(
    fn: (Long) -> DOMAIN,
    validation: Validation<Long> = ::alwaysValid,
    masking: Masking<Long> = Maskers.public
) : ValueFactory<DOMAIN, Long>(fn, String::toLong, validation, masking)

open class DoubleValueFactory<DOMAIN>(
    fn: (Double) -> DOMAIN,
    validation: Validation<Double> = ::alwaysValid,
    masking: Masking<Double> = Maskers.public
) : ValueFactory<DOMAIN, Double>(fn, String::toDouble, validation, masking)

open class FloatValueFactory<DOMAIN>(
    fn: (Float) -> DOMAIN,
    validation: Validation<Float> = ::alwaysValid,
    masking: Masking<Float> = Maskers.public
) : ValueFactory<DOMAIN, Float>(fn, String::toFloat, validation, masking)

open class BooleanValueFactory<DOMAIN>(
    fn: (Boolean) -> DOMAIN,
    validation: Validation<Boolean> = ::alwaysValid,
    masking: Masking<Boolean> = Maskers.public
) : ValueFactory<DOMAIN, Boolean>(fn, String::toBoolean, validation, masking)

open class BigIntegerValueFactory<DOMAIN>(
    fn: (BigInteger) -> DOMAIN,
    validation: Validation<BigInteger> = ::alwaysValid,
    masking: Masking<BigInteger> = Maskers.public
) : ValueFactory<DOMAIN, BigInteger>(fn, String::toBigInteger, validation, masking)

open class BigDecimalValueFactory<DOMAIN>(
    fn: (BigDecimal) -> DOMAIN,
    validation: Validation<BigDecimal> = ::alwaysValid,
    masking: Masking<BigDecimal> = Maskers.public
) : ValueFactory<DOMAIN, BigDecimal>(fn, String::toBigDecimal, validation, masking)

open class UUIDValueFactory<DOMAIN>(
    fn: (UUID) -> DOMAIN,
    validation: Validation<UUID> = ::alwaysValid,
    masking: Masking<UUID> = Maskers.public
) : ValueFactory<DOMAIN, UUID>(fn, UUID::fromString, validation, masking)

open class URLValueFactory<DOMAIN>(
    fn: (URL) -> DOMAIN,
    validation: Validation<URL> = ::alwaysValid,
    masking: Masking<URL> = Maskers.public
) : ValueFactory<DOMAIN, URL>(fn, ::URL, validation, masking)

open class DurationValueFactory<DOMAIN>(
    fn: (Duration) -> DOMAIN,
    validation: Validation<Duration> = ::alwaysValid,
    masking: Masking<Duration> = Maskers.public
) : ValueFactory<DOMAIN, Duration>(fn, Duration::parse, validation, masking)

open class InstantValueFactory<DOMAIN>(
    fn: (Instant) -> DOMAIN,
    validation: Validation<Instant> = ::alwaysValid,
    masking: Masking<Instant> = Maskers.public
) : ValueFactory<DOMAIN, Instant>(fn, Instant::parse, validation, masking)

open class LocalDateValueFactory<DOMAIN>(
    fn: (LocalDate) -> DOMAIN,
    validation: Validation<LocalDate> = ::alwaysValid,
    masking: Masking<LocalDate> = Maskers.public,
    formatter: DateTimeFormatter = ISO_LOCAL_DATE
) : ValueFactory<DOMAIN, LocalDate>(fn, { LocalDate.parse(it, formatter) }, validation, masking)

open class LocalTimeValueFactory<DOMAIN>(
    fn: (LocalTime) -> DOMAIN,
    validation: Validation<LocalTime> = ::alwaysValid,
    masking: Masking<LocalTime> = Maskers.public,
    formatter: DateTimeFormatter = ISO_LOCAL_TIME
) : ValueFactory<DOMAIN, LocalTime>(fn, { LocalTime.parse(it, formatter) }, validation, masking)

open class LocalDateTimeValueFactory<DOMAIN>(
    fn: (LocalDateTime) -> DOMAIN,
    validation: Validation<LocalDateTime> = ::alwaysValid,
    masking: Masking<LocalDateTime> = Maskers.public,
    formatter: DateTimeFormatter = ISO_LOCAL_DATE_TIME
) : ValueFactory<DOMAIN, LocalDateTime>(fn, { LocalDateTime.parse(it, formatter) }, validation, masking)

open class OffsetDateTimeValueFactory<DOMAIN>(
    fn: (OffsetDateTime) -> DOMAIN,
    validation: Validation<OffsetDateTime> = ::alwaysValid,
    masking: Masking<OffsetDateTime> = Maskers.public,
    formatter: DateTimeFormatter = ISO_OFFSET_DATE_TIME
) : ValueFactory<DOMAIN, OffsetDateTime>(fn, { OffsetDateTime.parse(it, formatter) }, validation, masking)

open class OffsetTimeValueFactory<DOMAIN>(
    fn: (OffsetTime) -> DOMAIN,
    validation: Validation<OffsetTime> = ::alwaysValid,
    masking: Masking<OffsetTime> = Maskers.public,
    formatter: DateTimeFormatter = ISO_OFFSET_TIME
) : ValueFactory<DOMAIN, OffsetTime>(fn, { OffsetTime.parse(it, formatter) }, validation, masking)

open class PeriodValueFactory<DOMAIN>(
    fn: (Period) -> DOMAIN,
    validation: Validation<Period> = ::alwaysValid,
    masking: Masking<Period> = Maskers.public
) : ValueFactory<DOMAIN, Period>(fn, Period::parse, validation, masking)

open class YearMonthValueFactory<DOMAIN>(
    fn: (YearMonth) -> DOMAIN,
    validation: Validation<YearMonth> = ::alwaysValid,
    masking: Masking<YearMonth> = Maskers.public,
    formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM")
) : ValueFactory<DOMAIN, YearMonth>(fn, { YearMonth.parse(it, formatter) }, validation, masking)

open class YearValueFactory<DOMAIN>(
    fn: (Year) -> DOMAIN,
    validation: Validation<Year> = ::alwaysValid,
    masking: Masking<Year> = Maskers.public,
    formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy")
) : ValueFactory<DOMAIN, Year>(fn, { Year.parse(it, formatter) }, validation, masking)

open class ZonedDateTimeValueFactory<DOMAIN>(
    fn: (ZonedDateTime) -> DOMAIN,
    validation: Validation<ZonedDateTime> = ::alwaysValid,
    masking: Masking<ZonedDateTime> = Maskers.public,
    formatter: DateTimeFormatter = ISO_ZONED_DATE_TIME
) : ValueFactory<DOMAIN, ZonedDateTime>(fn, { ZonedDateTime.parse(it, formatter) }, validation, masking)
