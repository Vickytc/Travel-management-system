package com.travel.admin.utils;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5Util {
    /**
     * Default password string combination, used to convert bytes into hexadecimal representation characters.
     * This combination is used by Apache to verify the correctness of downloaded files.
     */
    protected static char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    protected static MessageDigest messagedigest = null;

    static {
        try {
            messagedigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException nsaex) {
            System.err.println(Md5Util.class.getName() + " initialization failed, MessageDigest does not support MD5.");
            nsaex.printStackTrace();
        }
    }

    /**
     * Generates the MD5 checksum of a string.
     *
     * @param s the input string
     * @return the MD5 checksum
     */
    public static String getMD5String(String s) {
        return getMD5String(s.getBytes());
    }

    /**
     * Checks if the MD5 checksum of a string matches a known MD5 checksum.
     *
     * @param password  the string to check
     * @param md5PwdStr the known MD5 checksum
     * @return true if they match, false otherwise
     */
    public static boolean checkPassword(String password, String md5PwdStr) {
        String s = getMD5String(password);
        return s.equals(md5PwdStr);
    }

    public static String getMD5String(byte[] bytes) {
        messagedigest.update(bytes);
        return bufferToHex(messagedigest.digest());
    }

    private static String bufferToHex(byte bytes[]) {
        return bufferToHex(bytes, 0, bytes.length);
    }

    private static String bufferToHex(byte bytes[], int m, int n) {
        StringBuffer stringbuffer = new StringBuffer(2 * n);
        int k = m + n;
        for (int l = m; l < k; l++) {
            appendHexPair(bytes[l], stringbuffer);
        }
        return stringbuffer.toString();
    }

    private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
        char c0 = hexDigits[(bt & 0xf0) >> 4]; // Convert the high 4 bits of the byte
        char c1 = hexDigits[bt & 0xf]; // Convert the low 4 bits of the byte
        stringbuffer.append(c0);
        stringbuffer.append(c1);
    }
}

