package pl.stosik.url.shortener.domain.strategies;

import pl.stosik.url.shortener.domain.UrlShorteningStrategy;

import java.util.concurrent.atomic.AtomicInteger;

// shortUrl + some number from 1..n
public class RandomNumberUrlShorteningStrategy implements UrlShorteningStrategy {

    private final AtomicInteger n = new AtomicInteger(0);

    @Override
    public String shorten(String longUrl) {
        return longUrl + n.incrementAndGet();
    }
}
