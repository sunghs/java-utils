package sunghs.java.utils.cipher;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class Aes128Test {

    @Test
    public void test() throws Exception {
        Aes128 aes128 = new Aes128("password12345678");
        String enc = aes128.encrypt("this is plain text");
        String dec = aes128.decrypt(enc);
        log.info(enc);
        log.info(dec);
    }
}
