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
package com.chh.service;

import com.chh.config.properties.SecurityEncryptConfig;
import com.chh.encryption.Encryption;
import com.chh.factory.EncryptionServiceFactory;
import com.chh.util.SecurityData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 陈汉辉
 * @since 1.0.0
 */
@Service
public class EncryptionService implements Encryption {

    private final Encryption encryption;

    @Autowired
    public EncryptionService(SecurityEncryptConfig secretEncryptConfig) {
        this.encryption = new EncryptionServiceFactory(secretEncryptConfig).createEncryptionService();
    }

    @Override
    public SecurityData encrypt(String plainText) {
        return encryption.encrypt(plainText);
    }

    @Override
    public String decrypt(SecurityData securityData) {
        return encryption.decrypt(securityData);
    }

}
