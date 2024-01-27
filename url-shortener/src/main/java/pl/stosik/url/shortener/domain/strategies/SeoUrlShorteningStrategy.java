package pl.stosik.url.shortener.domain.strategies;

import pl.stosik.url.shortener.domain.UrlShorteningStrategy;

// shortUrl + seoKeyword
public class SeoUrlShorteningStrategy implements UrlShorteningStrategy {

    private static final String SEO_KEYWORD = "seoKeyword";

    @Override
    public String shorten(String longUrl) {
        return longUrl + SEO_KEYWORD;
    }
}
