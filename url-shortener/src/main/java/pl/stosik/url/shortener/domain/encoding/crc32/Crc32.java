package pl.stosik.url.shortener.domain.encoding.crc32;

import pl.stosik.url.shortener.domain.encoding.Encoder;

import java.util.UUID;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

public class Crc32 implements Encoder {

    public String encode(UUID uuid) {
        byte[] bytes = uuid.toString().getBytes();
        Checksum checksum = new CRC32();
        checksum.update(bytes, 0, bytes.length);

        return String.valueOf(checksum.getValue());
    }
}
