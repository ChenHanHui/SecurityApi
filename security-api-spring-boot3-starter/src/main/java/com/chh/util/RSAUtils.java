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

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 陈汉辉
 * @since 1.0.0
 */
public class RSAUtils {

    // 签名算法名称
    private static final String RSA_KEY_ALGORITHM = "RSA/ECB/PKCS1Padding";

    public static final String RSA1_SIGNATURE_ALGORITHM = "SHA1withRSA";
    public static final String RSA256_SIGNATURE_ALGORITHM = "SHA256withRSA";
    public static final String RSA384_SIGNATURE_ALGORITHM = "SHA384withRSA";
    public static final String RSA512_SIGNATURE_ALGORITHM = "SHA512withRSA";

    // RSA密钥长度,默认密钥长度是1024,密钥长度必须是64的倍数，在512到65536位之间,不管是RSA还是RSA2长度推荐使用2048
    private static final int KEY_SIZE = 2048;
    private static final int[] KEY_SIZES = {512, 1024, 2048, 4096, 8192, 16384};

    public RSAUtils() {
    }

    /**
     * 生成密钥对
     *
     * @return 返回包含公私钥的map
     */
    public static Map<String, String> generateKeyPair() {
        return generateKeyPair(KEY_SIZE);
    }

    public static Map<String, String> generateKeyPair(int keySize) {
        if (!isValidKeySize(keySize)) {
            throw new IllegalArgumentException("Invalid key size: " + keySize + ", must be " + Arrays.toString(KEY_SIZES));
        }
        try {
            KeyPairGenerator keygen = KeyPairGenerator.getInstance("RSA");
            SecureRandom secureRandom = new SecureRandom();
            keygen.initialize(keySize, secureRandom);
            KeyPair keyPair = keygen.genKeyPair();
            Map<String, String> keyPairMap = new HashMap<>();
            keyPairMap.put("publicKey", Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded()));
            keyPairMap.put("privateKey", Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded()));
            return keyPairMap;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to generate RSA key pair", e);
        }
    }

    public static boolean isValidKeySize(int keySize) {
        for (int size : KEY_SIZES) {
            if (size == keySize) {
                return true;
            }
        }
        return false;
    }

    /**
     * 根据signType自动选择签名算法
     *
     * @return 默认签名算法
     */
    private static String defaultSignatureAlgorithm(String signType) {
        return switch (signType) {
            case RSA256_SIGNATURE_ALGORITHM -> RSA256_SIGNATURE_ALGORITHM;
            case RSA384_SIGNATURE_ALGORITHM -> RSA384_SIGNATURE_ALGORITHM;
            case RSA512_SIGNATURE_ALGORITHM -> RSA512_SIGNATURE_ALGORITHM;
            default -> RSA1_SIGNATURE_ALGORITHM;
        };
    }

    /**
     * 公钥加密(用于数据加密)
     *
     * @param data         待加密数据
     * @param publicKeyStr 公钥字符串
     * @return 加密后的数据
     */
    public static String encryptByPublicKey(String data, String publicKeyStr) {
        try {
            // 获取公钥对象
            PublicKey publicKey = generatePublicKeyFromBase64(publicKeyStr);
            // 根据名称获取密码对象Cipher（转换的名称：算法/工作模式/填充模式）
            Cipher cipher = Cipher.getInstance(RSA_KEY_ALGORITHM);
            // 用公钥初始化此Cipher对象（加密模式）
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            // 对数据加密
            byte[] encryptedBytes = cipher.doFinal(data.getBytes());
            // 返回base64编码后的字符串
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException("Failed to encrypt data with public key", e);
        }
    }

    /**
     * 私钥解密(用于数据加密)
     *
     * @param data          待解密数据
     * @param privateKeyStr 私钥字符串
     * @return 解密后的数据
     */
    public static String decryptByPrivateKey(String data, String privateKeyStr) {
        try {
            // 获取私钥对象
            PrivateKey privateKey = generatePrivateKeyFromBase64(privateKeyStr);
            // 根据名称获取密码对象Cipher（转换的名称：算法/工作模式/填充模式）
            Cipher cipher = Cipher.getInstance(RSA_KEY_ALGORITHM);
            // 用私钥初始化此Cipher对象（解密模式）
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            // 对数据解密
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(data));
            // 返回字符串
            return new String(decryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException("Failed to decrypt data with private key", e);
        }
    }

    /**
     * 私钥加密(用于数据签名)
     *
     * @param data          待加密数据
     * @param privateKeyStr 私钥字符串
     * @return 加密后的数据
     */
    public static String encryptByPrivateKey(String data, String privateKeyStr) {
        try {
            // 获取私钥对象
            PrivateKey privateKey = generatePrivateKeyFromBase64(privateKeyStr);
            // 根据名称获取密码对象Cipher（转换的名称：算法/工作模式/填充模式）
            Cipher cipher = Cipher.getInstance(RSA_KEY_ALGORITHM);
            //用私钥初始化此Cipher对象（加密模式）
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            //对数据加密
            byte[] encrypt = cipher.doFinal(data.getBytes());
            //返回base64编码后的字符串
            return Base64.getEncoder().encodeToString(encrypt);
        } catch (Exception e) {
            throw new RuntimeException("Failed to encrypt data with private key", e);
        }
    }

    /**
     * 公钥解密(用于数据验签)
     *
     * @param data         待解密数据
     * @param publicKeyStr 公钥字符串
     * @return 解密后的数据
     */
    public static String decryptByPublicKey(String data, String publicKeyStr) {
        try {
            // 获取公钥对象
            PublicKey publicKey = generatePublicKeyFromBase64(publicKeyStr);
            //根据转换的名称获取密码对象Cipher（转换的名称：算法/工作模式/填充模式）
            Cipher cipher = Cipher.getInstance(RSA_KEY_ALGORITHM);
            //用公钥初始化此Cipher对象（解密模式）
            cipher.init(Cipher.DECRYPT_MODE, publicKey);
            //对数据解密
            byte[] decrypt = cipher.doFinal(Base64.getDecoder().decode(data));
            //返回字符串
            return new String(decrypt);
        } catch (Exception e) {
            throw new RuntimeException("Failed to decrypt data with public key", e);
        }
    }

    /**
     * RSA签名
     *
     * @param data     待签名数据
     * @param priKey   私钥
     * @param signType 签名算法类型
     * @return 签名
     */
    public static String sign(byte[] data, byte[] priKey, String signType) {
        try {
            // 获取私钥对象
            PrivateKey privateKey = generatePrivateKey(priKey);
            // 用指定算法产生签名对象Signature
            Signature signature = Signature.getInstance(defaultSignatureAlgorithm(signType));
            // 用私钥初始化签名对象Signature
            signature.initSign(privateKey);
            // 将待签名的数据传送给签名对象(须在初始化之后)
            signature.update(data);
            // 返回签名结果字节数组
            byte[] sign = signature.sign();
            // 返回Base64编码后的字符串
            return Base64.getEncoder().encodeToString(sign);
        } catch (Exception e) {
            throw new RuntimeException("Failed to sign data", e);
        }
    }

    /**
     * RSA校验数字签名
     *
     * @param data     待校验数据
     * @param sign     数字签名
     * @param pubKey   公钥
     * @param signType 签名算法类型
     * @return 验证结果
     */
    public static boolean verify(byte[] data, byte[] sign, byte[] pubKey, String signType) {
        try {
            // 获取公钥对象
            PublicKey publicKey = generatePublicKey(pubKey);
            // 用指定算法产生签名对象Signature
            Signature signature = Signature.getInstance(defaultSignatureAlgorithm(signType));
            // 用公钥初始化签名对象,用于验证签名
            signature.initVerify(publicKey);
            // 更新签名内容
            signature.update(data);
            // 得到验证结果
            return signature.verify(sign);
        } catch (Exception e) {
            throw new RuntimeException("Failed to verify signature", e);
        }
    }

    /**
     * 公钥分段加密(用于数据加密)
     *
     * @param data         待加密数据
     * @param publicKeyStr 公钥字符串
     * @return 加密后的数据
     */
    public static String segmentedEncryptByPublicKey(String data, String publicKeyStr, int keySize) {
        try {
            // 获取公钥对象
            PublicKey publicKey = generatePublicKeyFromBase64(publicKeyStr);
            // 根据名称获取密码对象Cipher（转换的名称：算法/工作模式/填充模式）
            Cipher cipher = Cipher.getInstance(RSA_KEY_ALGORITHM);
            // 用公钥初始化此Cipher对象（加密模式）
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            String[] blocks = StringUtils.splitStringByLength(data, (int) Math.floor((double) (keySize / 8 - 11) / 3));
            StringBuilder encryptData = new StringBuilder();
            for (String block : blocks) {
                byte[] encryptBlock = cipher.doFinal(block.getBytes(StandardCharsets.UTF_8));
                encryptData.append(Base64Utils.encodeToString(encryptBlock)).append(";");
            }
            // 返回base64编码后的字符串
            return encryptData.substring(0, encryptData.length() - 1);
        } catch (Exception e) {
            throw new RuntimeException("Failed to encrypt data with public key", e);
        }
    }

    /**
     * 私钥分段解密(用于数据加密)
     *
     * @param data          待解密数据
     * @param privateKeyStr 私钥字符串
     * @return 解密后的数据
     */
    public static String segmentedDecryptByPrivateKey(String data, String privateKeyStr) {
        try {
            PrivateKey privateKey = generatePrivateKeyFromBase64(privateKeyStr); // 假设这个方法能正确生成私钥
            Cipher cipher = Cipher.getInstance(RSA_KEY_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            String[] blocks = data.split(";");
            StringBuilder decryptedData = new StringBuilder();
            for (String block : blocks) {
                byte[] decryptedBlock = cipher.doFinal(Base64Utils.decode(block.getBytes(StandardCharsets.UTF_8)));
                decryptedData.append(new String(decryptedBlock));
            }
            return decryptedData.toString();
        } catch (Exception e) {
            throw new RuntimeException("Failed to decrypt data with private key", e);
        }
    }

    /**
     * 生成私钥
     */
    private static PrivateKey generatePrivateKey(byte[] encodedPrivateKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        // 创建PKCS8编码密钥规范
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(encodedPrivateKey);
        // 返回转换指定算法的KeyFactory对象
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);
    }

    /**
     * 生成公钥
     */
    private static PublicKey generatePublicKey(byte[] encodedPublicKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        // 创建X509编码密钥规范
        X509EncodedKeySpec spec = new X509EncodedKeySpec(encodedPublicKey);
        // 返回转换指定算法的KeyFactory对象
        KeyFactory kf = KeyFactory.getInstance("RSA");
        // 根据X509编码密钥规范产生公钥对象
        return kf.generatePublic(spec);
    }

    /**
     * 将私钥字符串base64编码之后生成私钥对象
     */
    private static PrivateKey generatePrivateKeyFromBase64(String privateKeyStr) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return generatePrivateKey(Base64.getDecoder().decode(privateKeyStr));
    }

    /**
     * 将公钥字符串base64编码之后生成公钥对象
     */
    private static PublicKey generatePublicKeyFromBase64(String publicKeyStr) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return generatePublicKey(Base64.getDecoder().decode(publicKeyStr));
    }

}

