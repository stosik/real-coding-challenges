package pl.stosik.url.shortener.domain.strategies;

import pl.stosik.url.shortener.domain.UrlShorteningStrategy;

import java.security.SecureRandom;

// shortUrl + 4 random alphanumeric characters
public class RandomAlphanumericUrlShorteningStrategy implements UrlShorteningStrategy {

    private static final int ALPHANUMERIC_CHARS = 4;
    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    @Override
    public String shorten(String longUrl) {
        return longUrl + generateRandomAlphanumeric();
    }

    private String generateRandomAlphanumeric() {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(ALPHANUMERIC_CHARS);
        for (int i = 0; i < ALPHANUMERIC_CHARS; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(randomIndex));
        }
        return sb.toString();
    }

}
