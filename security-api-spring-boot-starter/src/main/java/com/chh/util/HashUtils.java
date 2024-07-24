package com.chh.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class HashUtils {

    private static final String[] SUPPORTED_ALGORITHMS = {"SHA-256", "SHA-384", "SHA-512", "SHA3-256", "SHA3-384", "SHA3-512"};

    public HashUtils(){
    }

    /**
     * 计算给定字符串的指定算法的散列值。
     *
     * @param input 输入字符串
     * @param algorithm 散列算法名称
     * @return 返回Base64编码后的散列值字符串
     */
    public static String computeHash(String input, String algorithm) {
        if (!isSupportedAlgorithm(algorithm)) {
            throw new IllegalArgumentException("Unsupported algorithm: " + algorithm);
        }

        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Algorithm not found: " + algorithm, e);
        }
    }

    /**
     * 检查算法是否受支持。
     *
     * @param algorithm 散列算法名称
     * @return 如果算法受支持则返回true，否则返回false
     */
    private static boolean isSupportedAlgorithm(String algorithm) {
        for (String alg : SUPPORTED_ALGORITHMS) {
            if (alg.equalsIgnoreCase(algorithm)) {
                return true;
            }
        }
        return false;
    }

}
