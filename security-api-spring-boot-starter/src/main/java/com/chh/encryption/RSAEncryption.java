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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
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

    public RSAEncryption(SecretEncryptConfig secretEncryptConfig) {
        if (StringUtils.isBlank(secretEncryptConfig.getRsa().getPrivateKey())) {
            Map<String, String> keyMap = RSAUtils.generateKeyPair();
            String publicKey = keyMap.get("publicKey");
            String privateKey = keyMap.get("privateKey");
            secretEncryptConfig.getRsa().setPrivateKey(privateKey);
            log.info("The configure security.encrypt.rsa.privateKey parameter is empty! Use the default key pair.\npublicKey: {}\nprivateKey: {}", publicKey, privateKey);
            log.info("The configure security.encrypt.rsa.privateKey parameter have automatic assembly!");
        }
        if (!RSAUtils.isValidKeySize(secretEncryptConfig.getRsa().getKeySize())) {
            throw new IllegalArgumentException("The configure security.encrypt.rsa.keySize is invalid: " +
                    secretEncryptConfig.getRsa().getKeySize() + ", must be [512, 1024, 2048, 4096, 8192, 16384]");
        }
        this.rsa = secretEncryptConfig.getRsa();
        limitCharCount = (int) Math.floor((double) (rsa.getKeySize() / 8 - 11) / 3);
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
                        HashUtils.computeHash(encrypt, "SHA-256").getBytes(),
                        Base64.getDecoder().decode(rsa.getPrivateKey()),
                        RSAUtils.RSA256_SIGNATURE_ALGORITHM
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
                        HashUtils.computeHash(securityData.getContent(), "SHA-256").getBytes(),
                        Base64.getDecoder().decode(securityData.getSign()),
                        Base64.getDecoder().decode(getClientPublicKey()),
                        RSAUtils.RSA256_SIGNATURE_ALGORITHM
                );
                if (!verify) {
                    throw new SecurityBadException("The signature verification failed!");
                }
            }
            return RSAUtils.segmentedDecryptByPrivateKey(
                    securityData.getContent(),
                    rsa.getPrivateKey()
            );
        } catch (SecurityBadException e) {
            throw new SecurityBadException(e.getMessage(), e);
        } catch (Exception e) {
            throw new SecurityException(e);
        }
    }

    private String getClientPublicKey() {
        if (rsa.getClientPublicKey() != null) {
            return rsa.getClientPublicKey();
        }
        HttpServletRequest request = ServletUtils.getRequest();
        Object publicKey = request.getAttribute(SecurityConstant.CLIENT_PUBLIC_KEY);
        if (publicKey == null || StringUtils.isBlank(publicKey.toString())) {
            log.error("Please set request.setAttribute(SecurityConstant.CLIENT_PUBLIC_KEY, clientPublicKey) in @ModelAttribute");
            throw new SecurityException("Request body decryption failed");
        }
        return publicKey.toString();
    }

}
