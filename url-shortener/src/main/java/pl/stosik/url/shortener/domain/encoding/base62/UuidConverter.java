package pl.stosik.url.shortener.domain.encoding.base62;

import java.math.BigInteger;
import java.util.UUID;

class UuidConverter {

    static BigInteger toBigInteger(UUID uuid) {
        return BigIntegerPairing.pair(
                BigInteger.valueOf(uuid.getMostSignificantBits()),
                BigInteger.valueOf(uuid.getLeastSignificantBits())
        );
    }
}
