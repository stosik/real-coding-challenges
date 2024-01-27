package pl.stosik.url.shortener.domain;

import pl.stosik.url.shortener.domain.encoding.Encoder;
import pl.stosik.url.shortener.domain.encoding.UuidGenerator;
import pl.stosik.url.shortener.infrastracture.persistence.Storage;
import pl.stosik.url.shortener.model.UrlShorteningResult;

import java.util.UUID;

public class UrlShortener {

    private static final String FIXED_DOMAIN = "https//tinyurl.com/";
    private final UrlShorteningStrategy strategy;
    private final Storage storage;
    private final Encoder encoder;
    private final UuidGenerator uuidGenerator;

    public UrlShortener(UrlShorteningStrategy strategy, Storage storage, Encoder encoder, UuidGenerator uuidGenerator) {
        this.strategy = strategy;
        this.storage = storage;
        this.encoder = encoder;
        this.uuidGenerator = uuidGenerator;
    }

    public String shortenUrl(String longUrl) {
        return storage.find(longUrl).orElseGet(() -> generateAndStore(longUrl)).shortUrl();
    }

    private UrlShorteningResult generateAndStore(String longUrl) {
        // instead of UUID we could incorporate flink unique id generation with 64 bit ID
        // 1 sign bit | 41 bits timestamp | 5 bits data center id | 5 bits machine id | 12 bits seq number
        UUID key = uuidGenerator.generate();
        String encodedKey = encoder.encode(key);
        String shortenedUrl = strategy.shorten(FIXED_DOMAIN + encodedKey);
        UrlShorteningResult result = new UrlShorteningResult(encodedKey, longUrl, shortenedUrl);

        storage.save(result);

        return result;
    }
}
