package sunghs.java.utils.cipher;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5, SHA-256 암호화
 *
 * @author https://sunghs.tistory.com
 * @see <a href="https://github.com/sunghs/java-utils">source</a>
 */
public class HashCipher {

    private static final int UNSIGNED_BYTE = 0xff;

    private static final String ENCODING_TYPE = "UTF-8";

    public static String toMd5(final String str) {
        try {
            return encrypt(MessageDigest.getInstance("MD5"), str);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String toSha256(final String str) {
        try {
            return encrypt(MessageDigest.getInstance("SHA-256"), str);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String encrypt(MessageDigest digest, String str) throws UnsupportedEncodingException {
        StringBuilder buffer = new StringBuilder();
        final byte[] hash = digest.digest(str.getBytes(ENCODING_TYPE));
        for (byte b : hash) {
            buffer.append(hashToHex(b));
        }
        return buffer.toString();
    }

    private static String hashToHex(byte hash) {
        String hex = Integer.toHexString(UNSIGNED_BYTE & hash);
        return hex.length() == 1 ? ("0" + hex) : hex;
    }
}
