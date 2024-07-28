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

import java.util.Base64;
import java.util.Map;

public class RSAEncryption implements Encryption {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final RSA rsa;

    public RSAEncryption(SecretEncryptConfig secretEncryptConfig) {
        if (StringUtils.isBlank(secretEncryptConfig.getRsa().getPrivateKey())) {
            Map<String, String> keyMap = RSAUtils.generateKeyPair();
            String publicKey = keyMap.get("publicKey");
            String privateKey = keyMap.get("privateKey");
            secretEncryptConfig.getRsa().setPrivateKey(privateKey);
            log.info("The configure security.encrypt.rsa.privateKey parameter is empty! Use the default key pair.\npublicKey: {}\nprivateKey: {}", publicKey, privateKey);
            log.info("The configure security.encrypt.rsa.privateKey parameter have automatic assembly!");
        }
        this.rsa = secretEncryptConfig.getRsa();
    }

    @Override
    public SecurityData encrypt(String plainText) {
        try {
            String encrypt = RSAUtils.encryptByPublicKey(
                    plainText,
                    getClientPublicKey()
            );
            String sign = RSAUtils.sign(
                    HashUtils.computeHash(encrypt, "SHA-256").getBytes(),
                    Base64.getDecoder().decode(rsa.getPrivateKey()),
                    RSAUtils.RSA256_SIGNATURE_ALGORITHM
            );
            return new SecurityData(encrypt, sign);
        } catch (Exception e) {
            throw new SecurityException(e);
        }
    }

    @Override
    public String decrypt(SecurityData securityData) {
        if (StringUtils.isBlank(securityData.getSign())) {
            throw new SecurityBadException("The signature is empty!");
        }
        try {
            boolean verify = RSAUtils.verify(
                    HashUtils.computeHash(securityData.getContent(), "SHA-256").getBytes(),
                    Base64.getDecoder().decode(securityData.getSign()),
                    Base64.getDecoder().decode(getClientPublicKey()),
                    RSAUtils.RSA256_SIGNATURE_ALGORITHM
            );
            if (!verify) {
                throw new SecurityBadException("The signature verification failed!");
            }
            return RSAUtils.decryptByPrivateKey(
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
