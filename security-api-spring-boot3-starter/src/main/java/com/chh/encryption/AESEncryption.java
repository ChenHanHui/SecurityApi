package com.chh.encryption;

import com.chh.config.properties.AES;
import com.chh.config.properties.SecretEncryptConfig;
import com.chh.exception.SecurityException;
import com.chh.util.AESUtils;
import com.chh.util.SecurityData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.chh.util.StringUtils;

public class AESEncryption implements Encryption {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final AES aes;

    public AESEncryption(SecretEncryptConfig secretEncryptConfig) {
        if (StringUtils.isBlank(secretEncryptConfig.getAes().getKey())) {
            String aesKey = AESUtils.generateKey();
            secretEncryptConfig.getAes().setKey(aesKey);
            log.info("The configure security.encrypt.aes.key parameter is empty! Use the default key.\nAES Key: {}", aesKey);
            log.info("The configure security.encrypt.aes.key parameter have automatic assembly!");
            if (StringUtils.isBlank(secretEncryptConfig.getAes().getIv())) {
                String iv = AESUtils.generateIv();
                secretEncryptConfig.getAes().setIv(iv);
                log.info("The configure security.encrypt.aes.iv parameter is empty! Use the default iv.\nAES Iv: {}", iv);
                log.info("The configure security.encrypt.aes.iv parameter have automatic assembly!");
            }
        }
        this.aes = secretEncryptConfig.getAes();
    }

    @Override
    public SecurityData encrypt(String plainText) {
        try {
            return new SecurityData(
                    AESUtils.encrypt(plainText,aes.getKey(), aes.getIv()),
                    null
            );
        } catch (Exception e) {
            throw new SecurityException(e);
        }
    }

    @Override
    public String decrypt(SecurityData securityData) {
        try {
            return AESUtils.decrypt(
                    securityData.getContent(),
                    aes.getKey(),
                    aes.getIv()
            );
        } catch (Exception e) {
            throw new SecurityException(e);
        }
    }

}
