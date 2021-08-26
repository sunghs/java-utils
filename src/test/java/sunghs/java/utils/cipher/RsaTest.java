package sunghs.java.utils.cipher;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Optional;

@Slf4j
class RsaTest {

    @Test
    void createKeyPair() {
        KeyPair keyPair = Rsa.generateKey(1024);
        Optional.ofNullable(keyPair).ifPresent(k -> {
            PublicKey publicKey = k.getPublic();
            PrivateKey privateKey = k.getPrivate();

            log.info("public : {}", Rsa.encodeKey(publicKey));
            log.info("private : {}", Rsa.encodeKey(privateKey));
        });
    }

    @Test
    void test() {
        KeyPair keyPair = Rsa.generateKey(2048);
        Optional.ofNullable(keyPair).ifPresent(k -> {
            PublicKey publicKey = k.getPublic();
            PrivateKey privateKey = k.getPrivate();

            Rsa rsaForPublic = new Rsa(publicKey);
            Rsa rsaForPrivate = new Rsa(privateKey);

            /*
            평문은 public key로 암호화하고, private key로 복호화 한다.

            RSA 는 반대로 public 복호화, private 암호화가 가능하지만,
            대부분 암호화 하는 키를 공개키(public)로, 복호화 하는 키를 개인키(private) 정의 하기 때문에
            public으로 암호화, private으로 복호화 한다.
             */

            String plain = "test 1234567890 가나다라마바사아자차카타파하";

            try {
                String encrypted = rsaForPublic.encrypt(plain);
                log.info("encrypted : {}", encrypted);
                String decrypted = rsaForPrivate.decrypt(encrypted);
                log.info("decrypted : {}", decrypted);
            } catch (InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
                e.printStackTrace();
            }
        });
    }
}
