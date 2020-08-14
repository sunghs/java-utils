package sunghs.java.utils.cipher;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * RSA 암호화
 *
 * @author https://sunghs.tistory.com
 * @see <a href="https://github.com/sunghs/java-utils">source</a>
 */
public class Rsa {

    private static final Charset ENCODING_TYPE = StandardCharsets.UTF_8;

    private static final String INSTANCE_TYPE = "RSA";

    private final Class<?> keyClass;

    private final Key keyInstance;

    private Cipher cipher;

    public Rsa(final Key key) {
        if (key instanceof PublicKey) {
            keyClass = PublicKey.class;
            keyInstance = key;
        } else if (key instanceof PrivateKey) {
            keyClass = PrivateKey.class;
            keyInstance = key;
        } else {
            throw new ClassCastException("unknown key class");
        }

        try {
            cipher = Cipher.getInstance(INSTANCE_TYPE);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    /**
     * key 객체 String 형으로 인코딩
     *
     * @param key key
     * @return String encodedKey
     */
    public static String encodeKey(final Key key) {
        return new String(Base64.getEncoder().encode(key.getEncoded()));
    }

    /**
     * String 으로 인코딩 된 private key 를 객체로 변환
     *
     * @param encodedKey encodedKey
     * @return PrivateKey key
     */
    public static PrivateKey decodePrivateKey(final String encodedKey) {
        try {
            byte[] bKey = Base64.getDecoder().decode(encodedKey.getBytes(ENCODING_TYPE));
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(bKey);
            KeyFactory keyFactory = KeyFactory.getInstance(INSTANCE_TYPE);
            return keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * String 으로 인코딩 된 public key 를 객체로 변환
     *
     * @param encodedKey encodedKey
     * @return PublicKey key
     */
    public static PublicKey decodePublicKey(final String encodedKey) {
        try {
            byte[] bKey = Base64.getDecoder().decode(encodedKey.getBytes(ENCODING_TYPE));
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(bKey);
            KeyFactory keyFactory = KeyFactory.getInstance(INSTANCE_TYPE);
            return keyFactory.generatePublic(x509EncodedKeySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * PublicKey, PrivateKey 생성
     *
     * @param size 생성될 키의 지정 값 (비트 수), 1024, 2048 권장
     * @return KeyPair
     */
    public static KeyPair generateKey(int size) {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(INSTANCE_TYPE);
            keyPairGenerator.initialize(size);
            return keyPairGenerator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String encrypt(final String str) throws InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        if (keyClass.isAssignableFrom(PublicKey.class)) {
            cipher.init(Cipher.ENCRYPT_MODE, keyInstance);
            byte[] encrypted = cipher.doFinal(str.getBytes(ENCODING_TYPE));
            return new String(Base64.getEncoder().encode(encrypted), ENCODING_TYPE);
        } else {
            throw new ClassCastException("not public key set");
        }
    }

    public String decrypt(final String str) throws InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        if (keyClass.isAssignableFrom(PrivateKey.class)) {
            cipher.init(Cipher.DECRYPT_MODE, keyInstance);
            byte[] decrypted = Base64.getDecoder().decode(str.getBytes(ENCODING_TYPE));
            return new String(cipher.doFinal(decrypted), ENCODING_TYPE);
        } else {
            throw new ClassCastException("not private key set");
        }
    }
}
