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

import com.chh.config.properties.AES;
import com.chh.config.properties.SecretEncryptConfig;
import com.chh.exception.SecurityException;
import com.chh.util.AESUtils;
import com.chh.util.SecurityData;
import com.chh.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 陈汉辉
 * @since 1.0.0
 */
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
