package common;

import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * Created by inbor on 15.05.2016.
 */
public class getMD5 {


        private static MessageDigest digester;

        static {
            try {
                digester = MessageDigest.getInstance("MD5");
            }
            catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }

        public static String crypt(String str) {
            if (str == null || str.length() == 0) {
                throw new IllegalArgumentException("String to encript cannot be null or zero length");
            }

            digester.update(str.getBytes());
            byte[] hash = digester.digest();
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < hash.length; i++) {
                if ((0xff & hash[i]) < 0x10) {
                    hexString.append("0" + Integer.toHexString((0xFF & hash[i])));
                }
                else {
                    hexString.append(Integer.toHexString(0xFF & hash[i]));
                }
            }
            return hexString.toString();
        }
    }
