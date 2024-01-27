package pl.stosik;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import pl.stosik.url.shortener.domain.UrlShortener;
import pl.stosik.url.shortener.domain.UrlShorteningStrategy;
import pl.stosik.url.shortener.domain.encoding.Encoder;
import pl.stosik.url.shortener.domain.encoding.UuidGenerator;
import pl.stosik.url.shortener.domain.encoding.base62.Url62;
import pl.stosik.url.shortener.domain.strategies.RandomAlphanumericUrlShorteningStrategy;
import pl.stosik.url.shortener.domain.strategies.RandomNumberUrlShorteningStrategy;
import pl.stosik.url.shortener.domain.strategies.SeoUrlShorteningStrategy;
import pl.stosik.url.shortener.infrastracture.persistence.UrlsInMemoryStorage;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class UrlShortenerTest {

    private static final String FIXED_DOMAIN = "https//tinyurl.com/";

    @Test
    void shouldShortenUrlAddingSeoKeyword() {
        // given
        var url = "https://very-long-url.com/wkfjnwfdewlfknwaflkwnamelfkmwealkfnwaljfknewaljkfw";

        UrlShortener urlShortener = new UrlShortener(
                new SeoUrlShorteningStrategy(),
                new UrlsInMemoryStorage(),
                new Url62(),
                new UuidGenerator()
        );

        // when
        var shortenedUrl = urlShortener.shortenUrl(url);

        // then
        assertTrue(shortenedUrl.contains(FIXED_DOMAIN));
        assertTrue(shortenedUrl.substring(FIXED_DOMAIN.length()).contains("seoKeyword"));
    }

    @Test
    void shouldShortenUrlAddingAlphanumeric() {
        // given
        var url = "https://very-long-url.com/wkfjnwfdewlfknwaflkwnamelfkmwealkfnwaljfknewaljkfw";

        UrlShortener urlShortener = new UrlShortener(
                new RandomAlphanumericUrlShorteningStrategy(),
                new UrlsInMemoryStorage(),
                new Url62(),
                new UuidGenerator()
        );

        // when
        var shortenedUrl = urlShortener.shortenUrl(url);

        // then
        assertTrue(shortenedUrl.contains(FIXED_DOMAIN));
        assertTrue(shortenedUrl.substring(shortenedUrl.length() - 4).matches("[a-zA-Z0-9]+"));

    }

    @Test
    void shouldShortenUrlAddingRandomNumber() {
        // given
        var url = "https://very-long-url.com/wkfjnwfdewlfknwaflkwnamelfkmwealkfnwaljfknewaljkfw";

        UrlShortener urlShortener = new UrlShortener(
                new RandomNumberUrlShorteningStrategy(),
                new UrlsInMemoryStorage(),
                new Url62(),
                new UuidGenerator()
        );

        // when
        var shortenedUrl = urlShortener.shortenUrl(url);

        // then
        assertTrue(shortenedUrl.contains(FIXED_DOMAIN));
        assertTrue(shortenedUrl.endsWith("1"));
    }

    @Test
    void shouldNotShortenTheSameUrlTwice() {
        // given
        var url = "https://very-long-url.com/wkfjnwfdewlfknwaflkwnamelfkmwealkfnwaljfknewaljkfw";
        var uuid = UUID.randomUUID();

        UrlShorteningStrategy strategy = Mockito.mock(RandomNumberUrlShorteningStrategy.class);
        UuidGenerator uuidGenerator = Mockito.mock(UuidGenerator.class);
        Encoder encoder = mock(Url62.class);

        when(uuidGenerator.generate()).thenReturn(uuid);
        when(encoder.encode(uuid)).thenReturn("encodedvalue");
        when(strategy.shorten(FIXED_DOMAIN + "encodedvalue")).thenReturn(FIXED_DOMAIN + "encodedvalue1");


        UrlShortener urlShortener = new UrlShortener(
                strategy,
                new UrlsInMemoryStorage(),
                encoder,
                uuidGenerator
        );

        // when
        urlShortener.shortenUrl(url);
        urlShortener.shortenUrl(url);

        // then
        verify(strategy, Mockito.times(1)).shorten(FIXED_DOMAIN + "encodedvalue");
    }
}
