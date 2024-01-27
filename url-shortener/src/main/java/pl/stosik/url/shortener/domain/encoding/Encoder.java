package pl.stosik.url.shortener.domain.encoding;

import java.util.UUID;

public interface Encoder {

    String encode(UUID uuid);
}
