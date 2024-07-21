package com.chh.util;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

public class AESUtils {

    private static final String AES_ALGORITHM = "AES";
    private static final String CIPHER_TRANSFORMATION = "AES/CBC/PKCS5Padding";
    private static final int KEY_SIZE = 256; // 128, 192, or 256 bits
    private static final int[] KEY_SIZES = {128, 192, 256};

    AESUtils() {
    }

    /**
     * Generates a secure random AES key.
     */
    public static String generateKey() {
        return generateKey(KEY_SIZE);
    }

    public static String generateKey(int keySize) {
        if (!isValidKeySize(keySize)) {
            throw new IllegalArgumentException("Invalid key size: " + keySize + ", must be " + Arrays.toString(KEY_SIZES));
        }
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance(AES_ALGORITHM);
            SecureRandom secureRandom = SecureRandom.getInstanceStrong();
            keyGen.init(KEY_SIZE, secureRandom);
            byte[] key = keyGen.generateKey().getEncoded();
            return Base64.getEncoder().encodeToString(key).substring(0, KEY_SIZE / 8);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean isValidKeySize(int keySize) {
        for (int size : KEY_SIZES) {
            if (size == keySize) {
                return true;
            }
        }
        return false;
    }

    /**
     * Generates a secure random IV.
     */
    public static String generateIv() {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance(AES_ALGORITHM);
            SecureRandom secureRandom = SecureRandom.getInstanceStrong();
            keyGen.init(128, secureRandom);
            byte[] key = keyGen.generateKey().getEncoded();
            return Base64.getEncoder().encodeToString(key).substring(0, 128 / 8);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取 cipher
     */
    private static Cipher getCipher(String key, String iv, int model) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), AES_ALGORITHM);
        Cipher cipher = Cipher.getInstance(CIPHER_TRANSFORMATION);
        if (iv != null) {
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes(StandardCharsets.UTF_8));
            cipher.init(model, secretKeySpec, ivParameterSpec);
        } else {
            cipher.init(model, secretKeySpec);
        }
        return cipher;
    }

    /**
     * AES加密
     */
    public static String encrypt(String data, String key) throws Exception {
        return encrypt(data, key, null);
    }

    public static String encrypt(String data, String key, String iv) throws Exception {
        Cipher cipher = getCipher(key, iv, Cipher.ENCRYPT_MODE);
        byte[] valueByte = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return Base64Utils.encodeToString(valueByte);
    }

    /**
     * AES解密
     */
    public static String decrypt(String data, String key) throws Exception {
        return decrypt(data, key, null);
    }

    public static String decrypt(String data, String key, String iv) throws Exception {
        byte[] decodedData = Base64Utils.decodeFromString(data);
        Cipher cipher = getCipher(key, iv, Cipher.DECRYPT_MODE);
        byte[] decrypted = cipher.doFinal(decodedData);
        return new String(decrypted, StandardCharsets.UTF_8);
    }

}
