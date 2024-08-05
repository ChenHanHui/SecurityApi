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
package com.chh.encryption;

import com.chh.config.properties.RSA;
import com.chh.config.properties.SecretEncryptConfig;
import com.chh.constant.SecurityConstant;
import com.chh.exception.SecurityBadException;
import com.chh.exception.SecurityException;
import com.chh.util.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;
import java.util.Map;

/**
 * @author 陈汉辉
 * @since 1.0.0
 */
public class RSAEncryption implements Encryption {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final RSA rsa;
    private final int limitCharCount;
    private final PrivateKey privateKey;
    private PublicKey clientPublicKey = null;

    public RSAEncryption(SecretEncryptConfig secretEncryptConfig) {
        if (StringUtils.isBlank(secretEncryptConfig.getRsa().getPrivateKey())) {
            Map<String, String> keyMap = RSAUtils.generateKeyPair();
            String publicKey = keyMap.get("publicKey");
            String privateKey = keyMap.get("privateKey");
            secretEncryptConfig.getRsa().setPrivateKey(privateKey);
            log.info("""
                    The configure security.encrypt.rsa.privateKey parameter is empty! Use the default key pair.
                    publicKey: {}
                    privateKey: {}""", publicKey, privateKey);
            log.info("The configure security.encrypt.rsa.privateKey parameter have automatic assembly!");
        }
        if (!RSAUtils.isValidKeySize(secretEncryptConfig.getRsa().getKeySize())) {
            throw new IllegalArgumentException("The configure security.encrypt.rsa.keySize is invalid: " +
                    secretEncryptConfig.getRsa().getKeySize() + ", must be [512, 1024, 2048, 4096, 8192, 16384]");
        }
        rsa = secretEncryptConfig.getRsa();
        limitCharCount = (int) Math.floor((double) (rsa.getKeySize() / 8 - 11) / 3);
        if (rsa.getClientPublicKey() != null) {
            clientPublicKey = initPublicKey(rsa.getClientPublicKey());
        }
        privateKey = initPrivateKey(rsa.getPrivateKey());
    }

    @Override
    public SecurityData encrypt(String plainText) {
        try {
            String encrypt = RSAUtils.segmentedEncryptByPublicKey(
                    plainText,
                    getClientPublicKey(),
                    limitCharCount
            );
            if (rsa.getSign()) {
                String sign = RSAUtils.sign(
                        HashUtils.computeHash(
                                encrypt,
                                rsa.getSignAlgorithm().getAlgorithm()
                        ).getBytes(StandardCharsets.UTF_8),
                        privateKey,
                        rsa.getSignAlgorithm().name()
                );
                return new SecurityData(encrypt, sign);
            } else {
                return new SecurityData(encrypt, null);
            }
        } catch (Exception e) {
            throw new SecurityException(e);
        }
    }

    @Override
    public String decrypt(SecurityData securityData) {
        try {
            if (rsa.getSign()) {
                if (StringUtils.isBlank(securityData.getSign())) {
                    throw new SecurityBadException("The signature is empty!");
                }
                boolean verify = RSAUtils.verify(
                        HashUtils.computeHash(
                                securityData.getContent(),
                                rsa.getSignAlgorithm().getAlgorithm()
                        ).getBytes(StandardCharsets.UTF_8),
                        Base64.getDecoder().decode(securityData.getSign()),
                        getClientPublicKey(),
                        rsa.getSignAlgorithm().name()
                );
                if (!verify) {
                    throw new SecurityBadException("The signature verification failed!");
                }
            }
            return RSAUtils.segmentedDecryptByPrivateKey(
                    securityData.getContent(),
                    privateKey
            );
        } catch (SecurityBadException e) {
            throw new SecurityBadException(e.getMessage(), e);
        } catch (Exception e) {
            throw new SecurityException(e);
        }
    }

    private PublicKey getClientPublicKey() {
        if (rsa.getClientPublicKey() != null) {
            return clientPublicKey;
        }
        return initPublicKey(getRequestClientPublicKey());
    }

    private String getRequestClientPublicKey() {
        HttpServletRequest request = ServletUtils.getRequest();
        Object publicKey = request.getAttribute(SecurityConstant.CLIENT_PUBLIC_KEY);
        if (publicKey == null || StringUtils.isBlank(publicKey.toString())) {
            log.error("Please set request.setAttribute(SecurityConstant.CLIENT_PUBLIC_KEY, clientPublicKey) " +
                    "in @ModelAttribute");
            throw new SecurityException("Request body decryption failed");
        }
        return publicKey.toString();
    }

    private PublicKey initPublicKey(String publicKeyStr) {
        try {
            return RSAUtils.generatePublicKeyFromBase64(publicKeyStr);
        } catch (Exception e) {
            throw new RuntimeException("Failed to encrypt data with public key", e);
        }
    }

    private PrivateKey initPrivateKey(String privateKeyStr) {
        try {
            return RSAUtils.generatePrivateKeyFromBase64(privateKeyStr);
        } catch (Exception e) {
            throw new RuntimeException("Failed to decrypt data with private key", e);
        }
    }

}
