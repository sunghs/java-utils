package sunghs.java.utils.cipher;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
class Seed128Test {

    @Test
    void test() {
        Seed128 seed128 = new Seed128("password12345678");
        String plain = "this is plain text. 가나다라마바사. 123456";
        String enc = seed128.encrypt(plain);
        String dec = seed128.decrypt(enc);

        log.info(enc);
        log.info(dec);

        Assertions.assertEquals(plain, dec);
    }

    @Test
    void passwordOverLength() {
        // 유저 키가 16자리 초과 인 경우 에러
        Seed128 s1 = new Seed128("password1234567890qwertyui");
    }

    @Test
    void passwordUnderLength() {
        // 유저 키가 16자리 미만 인 경우 에러
        Seed128 s2 = new Seed128("password");
    }
}
