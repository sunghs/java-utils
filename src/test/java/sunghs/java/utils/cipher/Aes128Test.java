package sunghs.java.utils.cipher;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
class Aes128Test {

    @Test
    void test() throws Exception {
        Aes128 aes128 = new Aes128("password12345678");
        String given = "this is plain text";
        String enc = aes128.encrypt(given);
        String expected = aes128.decrypt(enc);
        Assertions.assertEquals(expected, given);
    }
}
