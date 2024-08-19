/*
 * Copyright 2024-2099 ChenHanHui
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.chh.util;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;

/**
 * @author 陈汉辉
 * @since 1.0.2
 */
public class SM4Utils {

    private static final String SM4_ALGORITHM = "SM4";
    private static final String CIPHER_ALGORITHM_ECB = "SM4/ECB/PKCS7Padding";
    private static final String CIPHER_ALGORITHM_CBC = "SM4/CBC/PKCS7Padding";
    private static final int DEFAULT_KEY_SIZE = 128;

    public SM4Utils() {
    }

    static {
        if (null == Security.getProvider(BouncyCastleProvider.PROVIDER_NAME)) {
            Security.addProvider(new BouncyCastleProvider());
        }
    }

    /**
     * 生成密钥
     */
    public static String generateKey() {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance(SM4_ALGORITHM);
            keyGen.init(DEFAULT_KEY_SIZE, SecureRandom.getInstanceStrong());
            byte[] key = keyGen.generateKey().getEncoded();
            return Base64Utils.encodeToString(key);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 生成iv
     */
    public static String generateIv() {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance(SM4_ALGORITHM);
            SecureRandom secureRandom = SecureRandom.getInstanceStrong();
            keyGen.init(128, secureRandom);
            byte[] key = keyGen.generateKey().getEncoded();
            return Base64Utils.encodeToString(key).substring(0, 128 / 8);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取Cipher
     */
    private static Cipher getCipher(String key, String iv, int model) throws Exception {
        SecretKey secretKey = new SecretKeySpec(Base64Utils.decodeFromString(key), SM4_ALGORITHM);
        Cipher cipher;
        if (iv != null) {
            cipher = Cipher.getInstance(CIPHER_ALGORITHM_CBC);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes(StandardCharsets.UTF_8));
            cipher.init(model, secretKey, ivParameterSpec);
        } else {
            cipher = Cipher.getInstance(CIPHER_ALGORITHM_ECB);
            cipher.init(model, secretKey);
        }
        return cipher;
    }

    /**
     * 加密
     */
    public static String encrypt(String data, String key, String iv) throws Exception {
        Cipher cipher = getCipher(key, iv, Cipher.ENCRYPT_MODE);
        byte[] encryptedByte = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return Base64Utils.encodeToString(encryptedByte);
    }

    /**
     * 解密
     */
    public static String decrypt(String data, String key, String iv) throws Exception {
        Cipher cipher = getCipher(key, iv, Cipher.DECRYPT_MODE);
        byte[] decryptedBytes = cipher.doFinal(Base64Utils.decodeFromString(data));
        return new String(decryptedBytes);
    }

}
