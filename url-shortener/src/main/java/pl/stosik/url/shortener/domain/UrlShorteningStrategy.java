package pl.stosik.url.shortener.domain;

public interface UrlShorteningStrategy {
    String shorten(String longUrl);
}
