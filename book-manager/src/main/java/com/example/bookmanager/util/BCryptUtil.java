package com.example.bookmanager.util;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.SecureRandom;
import java.util.Base64;

public class BCryptUtil {

    private static final int SALT_BYTES = 16;
    private static final int HASH_BYTES = 24;
    private static final int ITERATIONS = 1024;

    public static String encode(String password) {
        try {
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[SALT_BYTES];
            random.nextBytes(salt);

            PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATIONS, HASH_BYTES * 8);
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            byte[] hash = skf.generateSecret(spec).getEncoded();

            byte[] combined = new byte[salt.length + hash.length];
            // 完整5个参数：源数组、源起始、目标数组、目标起始、复制多少
            System.arraycopy(salt, 0, combined, 0, salt.length);
            System.arraycopy(hash, 0, combined, salt.length, hash.length);

            return Base64.getEncoder().encodeToString(combined);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException("加密失败", e);
        }
    }

    public static boolean matches(String rawPassword, String encodedPassword) {
        try {
            byte[] all = Base64.getDecoder().decode(encodedPassword);
            if (all.length <= SALT_BYTES) {
                return false;
            }

            byte[] salt = new byte[SALT_BYTES];
            byte[] oldHash = new byte[HASH_BYTES];
            System.arraycopy(all, 0, salt, 0, SALT_BYTES);
            System.arraycopy(all, SALT_BYTES, oldHash, 0, HASH_BYTES);

            PBEKeySpec spec = new PBEKeySpec(rawPassword.toCharArray(), salt, ITERATIONS, HASH_BYTES * 8);
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            byte[] newHash = skf.generateSecret(spec).getEncoded();

            return java.util.Arrays.equals(oldHash, newHash);
        } catch (Exception e) {
            return false;
        }
    }
}
