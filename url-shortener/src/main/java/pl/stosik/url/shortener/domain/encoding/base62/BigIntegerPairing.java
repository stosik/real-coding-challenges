package pl.stosik.url.shortener.domain.encoding.base62;


import java.math.BigInteger;
import java.util.function.Function;

/**
 * Basing on snippet published by drmalex07
 * <p>
 * https://gist.github.com/drmalex07/9008c611ffde6cb2ef3a2db8668bc251
 */
class BigIntegerPairing {

    private static final BigInteger HALF = BigInteger.ONE.shiftLeft(64); // 2^64

    private static final Function<BigInteger, BigInteger> toUnsigned = value -> value.signum() < 0 ? value.add(HALF) : value;

    static BigInteger pair(BigInteger hi, BigInteger lo) {
        BigInteger unsignedLo = toUnsigned.apply(lo);
        BigInteger unsignedHi = toUnsigned.apply(hi);
        return unsignedLo.add(unsignedHi.multiply(HALF));
    }
}