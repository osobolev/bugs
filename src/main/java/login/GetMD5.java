package login;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by inbor on 15.05.2016.
 */
public class GetMD5 {


    private static MessageDigest digester;
    private static Charset UTF8 = Charset.forName("UTF-8");

    static {
        try {
            digester = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public static String crypt(String str) throws UnsupportedEncodingException {
        if (str == null || str.length() == 0) {
            throw new IllegalArgumentException("String to encript cannot be null or zero length");
        }

        digester.update(str.getBytes(UTF8));
        byte[] hash = digester.digest();
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String strByte = Integer.toHexString(0xFF & b);
            if (strByte.length() < 2) {
                hexString.append('0').append(strByte);
            } else {
                hexString.append(strByte);
            }
        }
        return hexString.toString();
    }
}
