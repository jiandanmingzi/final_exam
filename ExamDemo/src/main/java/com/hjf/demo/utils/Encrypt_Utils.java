package com.hjf.demo.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Encrypt_Utils {
    public static String encrypt(String string) throws NoSuchAlgorithmException {
        MessageDigest digest =MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(string.getBytes());
        StringBuffer stringBuffer = new StringBuffer();
        for (byte b : hash) {
            stringBuffer.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
        }
        return stringBuffer.toString();
    }
}
