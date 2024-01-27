package pl.stosik.url.shortener.domain.encoding.base62;

import java.math.BigInteger;

/**
 * Base62 encoder/decoder.
 * <p>
 * This is free and unencumbered public domain software
 * <p>
 * Source: https://github.com/opencoinage/opencoinage/blob/master/src/java/org/opencoinage/util/Base62.java
 */
class Base62 {

    private static final BigInteger BASE = BigInteger.valueOf(62);
    private static final String DIGITS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    
    /**
     * Encodes a number using Base62 encoding.
     *
     * @param number a positive integer
     * @return a Base62 string
     * @throws IllegalArgumentException if <code>number</code> is a negative integer
     */
    static String encode(BigInteger number) {
        if (number.compareTo(BigInteger.ZERO) < 0) {
            throwIllegalArgumentException("number must not be negative");
        }
        StringBuilder result = new StringBuilder();
        while (number.compareTo(BigInteger.ZERO) > 0) {
            BigInteger[] divmod = number.divideAndRemainder(BASE);
            number = divmod[0];
            int digit = divmod[1].intValue();
            result.insert(0, DIGITS.charAt(digit));
        }
        return (result.length() == 0) ? DIGITS.substring(0, 1) : result.toString();
    }

    private static BigInteger throwIllegalArgumentException(String format, Object... args) {
        throw new IllegalArgumentException(String.format(format, args));
    }
}
