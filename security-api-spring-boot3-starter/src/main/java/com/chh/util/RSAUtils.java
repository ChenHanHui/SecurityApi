package com.chh.util;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

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

    // RSA最大加密明文大小
    private static final int MAX_ENCRYPT_BLOCK = 117;
    // RSA最大解密密文大小
    private static final int MAX_DECRYPT_BLOCK = 128;

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
            KeyPairGenerator keygen = KeyPairGenerator.getInstance(RSA_KEY_ALGORITHM);
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

    private static boolean isValidKeySize(int keySize) {
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
    public static String segmentedEncryptByPublicKey(String data, String publicKeyStr) {
        try {
            // 获取公钥对象
            PublicKey publicKey = generatePublicKeyFromBase64(publicKeyStr);
            // 根据名称获取密码对象Cipher（转换的名称：算法/工作模式/填充模式）
            Cipher cipher = Cipher.getInstance(RSA_KEY_ALGORITHM);
            // 用公钥初始化此Cipher对象（加密模式）
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            // 分段加密
            byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
            // 加密时超过 117 字节会报错，为此采用分段加密的办法来加密
            byte[] encryptedBytes = null;
            for (int i = 0; i < dataBytes.length; i += MAX_ENCRYPT_BLOCK) {
                // 注意要使用2的倍数，否则会出现加密后的内容再解密时为乱码
                byte[] doFinal = cipher.doFinal(ArrayUtils.subarray(dataBytes, i, i + MAX_ENCRYPT_BLOCK));
                encryptedBytes = ArrayUtils.addAll(encryptedBytes, doFinal);
            }
            // 返回base64编码后的字符串
            return Base64.getEncoder().encodeToString(encryptedBytes);
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
            // 获取私钥对象
            PrivateKey privateKey = generatePrivateKeyFromBase64(privateKeyStr);
            // 根据名称获取密码对象Cipher（转换的名称：算法/工作模式/填充模式）
            Cipher cipher = Cipher.getInstance(RSA_KEY_ALGORITHM);
            // 用私钥初始化此Cipher对象（解密模式）
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] dataBytes = Base64.getDecoder().decode(data);
            int inputLen = dataBytes.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段解密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                    cache = cipher.doFinal(dataBytes, offSet, MAX_DECRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(dataBytes, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_DECRYPT_BLOCK;
            }
            String decryptedData = out.toString(StandardCharsets.UTF_8);
            out.close();
            return decryptedData;
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
        KeyFactory kf = KeyFactory.getInstance(RSA_KEY_ALGORITHM);
        return kf.generatePrivate(spec);
    }

    /**
     * 生成公钥
     */
    private static PublicKey generatePublicKey(byte[] encodedPublicKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        // 创建X509编码密钥规范
        X509EncodedKeySpec spec = new X509EncodedKeySpec(encodedPublicKey);
        // 返回转换指定算法的KeyFactory对象
        KeyFactory kf = KeyFactory.getInstance(RSA_KEY_ALGORITHM);
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

