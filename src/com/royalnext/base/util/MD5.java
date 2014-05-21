package com.royalnext.base.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {
    public static String getMD5(String src) {
        String result = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            String message = src;
            md.update(message.getBytes());
            byte[] toChapter1Digest = md.digest();
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < toChapter1Digest.length; i++) {
                hexString.append(Integer.toHexString(0xFF & toChapter1Digest[i]));
            }
            for (int i = 0; i < toChapter1Digest.length; i++) {
                result += Integer.toString((toChapter1Digest[i] & 0xff) + 0x100, 16).substring(1);
            }
        } catch (NoSuchAlgorithmException e) {
        }
        return result;

    }
}
