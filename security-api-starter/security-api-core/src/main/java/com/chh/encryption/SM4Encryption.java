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

import com.chh.config.properties.SM4;
import com.chh.config.properties.SecurityEncryptConfig;
import com.chh.exception.SecurityException;
import com.chh.util.SM4Utils;
import com.chh.util.SecurityData;
import com.chh.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 陈汉辉
 * @since 1.0.2
 */
public class SM4Encryption implements Encryption {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final SM4 sm4;

    public SM4Encryption(SecurityEncryptConfig secretEncryptConfig) {
        if (StringUtils.isBlank(secretEncryptConfig.getSm4().getKey())) {
            String sm4Key = SM4Utils.generateKey();
            secretEncryptConfig.getSm4().setKey(sm4Key);
            log.info("The configure security.encrypt.sm4.key parameter is empty! Use the default key.\nSM4 Key: {}", sm4Key);
            log.info("The configure security.encrypt.sm4.key parameter have automatic assembly!");
            if (StringUtils.isBlank(secretEncryptConfig.getSm4().getIv())) {
                String iv = SM4Utils.generateIv();
                secretEncryptConfig.getSm4().setIv(iv);
                log.info("The configure security.encrypt.sm4.iv parameter is empty! Use the default iv.\nSM4 Iv: {}", iv);
                log.info("The configure security.encrypt.sm4.iv parameter have automatic assembly!");
            }
        }
        this.sm4 = secretEncryptConfig.getSm4();
    }

    @Override
    public SecurityData encrypt(String plainText) {
        try {
            return new SecurityData(
                    SM4Utils.encrypt(plainText, sm4.getKey(), sm4.getIv()),
                    null
            );
        } catch (Exception e) {
            throw new SecurityException(e);
        }
    }

    @Override
    public String decrypt(SecurityData securityData) {
        try {
            return SM4Utils.decrypt(
                    securityData.getContent(),
                    sm4.getKey(),
                    sm4.getIv()
            );
        } catch (Exception e) {
            throw new SecurityException(e);
        }
    }

}
