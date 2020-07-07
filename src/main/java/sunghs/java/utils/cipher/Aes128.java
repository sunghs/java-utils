package sunghs.java.utils.cipher;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * AES-128 암호화
 *
 * @author https://sunghs.tistory.com
 * @see <a href="https://github.com/sunghs/java-utils">source</a>
 */
public class Aes128 {

    private static final Charset ENCODING_TYPE = StandardCharsets.UTF_8;

    private static final String INSTANCE_TYPE = "AES/CBC/PKCS5Padding";

    private SecretKeySpec secretKeySpec;

    private Cipher cipher;

    private IvParameterSpec ivParameterSpec;

    public Aes128(final String key) {
        validation(key);
        try {
            byte[] keyBytes = key.getBytes(ENCODING_TYPE);
            secretKeySpec = new SecretKeySpec(keyBytes, "AES");
            cipher = Cipher.getInstance(INSTANCE_TYPE);
            ivParameterSpec = new IvParameterSpec(keyBytes);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public String encrypt(final String str) throws Exception {
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
        byte[] encrypted = cipher.doFinal(str.getBytes(ENCODING_TYPE));
        return new String(Base64.getEncoder().encode(encrypted), ENCODING_TYPE);
    }

    public String decrypt(final String str) throws Exception {
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
        byte[] decoded = Base64.getDecoder().decode(str.getBytes(ENCODING_TYPE));
        return new String(cipher.doFinal(decoded), ENCODING_TYPE);
    }

    private void validation(final String key) {
        Optional.ofNullable(key)
                .filter(Predicate.not(String::isBlank))
                .filter(Predicate.not(s -> s.length() != 16))
                .orElseThrow(IllegalArgumentException::new);
    }
}
