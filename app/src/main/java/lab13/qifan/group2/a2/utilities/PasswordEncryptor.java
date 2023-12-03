package lab13.qifan.group2.a2.utilities;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
public class PasswordEncryptor {

    public static String generateSalt() {
        byte[] salt = new byte[16];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(salt);
        return bytesToHex(salt);
    }

    public static String bytesToHex(byte[] byteStream){
        StringBuilder hex = new StringBuilder();
        for (byte b : byteStream) {
            hex.append(String.format("%02x", b));
        }
        return hex.toString();
    }
    public static byte[] hexToBytes(String hex) {
        int len = hex.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                    + Character.digit(hex.charAt(i + 1), 16));
        }
        return data;
    }
    public static String hashPassword(String password){
        String salt = generateSalt();
        try{
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(hexToBytes(salt));
            byte[] passwordHash = md.digest(password.getBytes());
            return salt + bytesToHex(passwordHash);
        } catch (NoSuchAlgorithmException e){
            e.printStackTrace();
            return null;
        }
    }
}
