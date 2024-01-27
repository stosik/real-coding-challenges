package pl.stosik.url.shortener.domain.encoding;

import java.util.UUID;

public class UuidGenerator {

    public UUID generate() {
        return UUID.randomUUID();
    }
}
