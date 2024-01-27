package pl.stosik.url.shortener.infrastracture.persistence;

import pl.stosik.url.shortener.model.UrlShorteningResult;

import java.util.Optional;

public interface Storage {
    void save(UrlShorteningResult result);

    Optional<UrlShorteningResult> find(String url);
}
