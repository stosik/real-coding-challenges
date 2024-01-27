package pl.stosik.url.shortener.domain.encoding.base62;

import pl.stosik.url.shortener.domain.encoding.Encoder;

import java.math.BigInteger;
import java.util.UUID;

public class Url62 implements Encoder {

    public String encode(UUID uuid) {
        BigInteger pair = UuidConverter.toBigInteger(uuid);
        return Base62.encode(pair);
    }
}
