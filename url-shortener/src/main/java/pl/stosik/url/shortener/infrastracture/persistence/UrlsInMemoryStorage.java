package pl.stosik.url.shortener.infrastracture.persistence;

import pl.stosik.url.shortener.model.UrlShorteningResult;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class UrlsInMemoryStorage implements Storage {

    private final ConcurrentMap<String, UrlShorteningResult> urls = new ConcurrentHashMap<>();

    @Override
    public void save(UrlShorteningResult result) {
        urls.putIfAbsent(result.longUrl(), result);
    }

    @Override
    public Optional<UrlShorteningResult> find(String url) {
        return Optional.ofNullable(urls.get(url));
    }
}
