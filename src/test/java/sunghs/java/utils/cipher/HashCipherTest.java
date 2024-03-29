package sunghs.java.utils.cipher;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
class HashCipherTest {

    String plain = "this is plain text. 이건 평문 입니다.";

    @Test
    void md5Test() {
        String hash = HashCipher.toMd5(plain);
        assert hash != null;
        Assertions.assertEquals("7169734D00087E487FF362FE8D14234F", hash.toUpperCase());
    }

    @Test
    void sha256Test() {
        String hash = HashCipher.toSha256(plain);
        assert hash != null;
        Assertions.assertEquals("CBEE28CB9D02F3E92E8DA297FA1FFA464BE85799A74E16526DB9DB7591B231EC", hash.toUpperCase());
    }
}
