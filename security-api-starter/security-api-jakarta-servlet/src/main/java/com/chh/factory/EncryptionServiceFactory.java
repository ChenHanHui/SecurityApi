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
package com.chh.factory;

import com.chh.config.properties.SecurityEncryptConfig;
import com.chh.encryption.AESEncryption;
import com.chh.encryption.Encryption;
import com.chh.encryption.RSAEncryption;
import com.chh.encryption.SM4Encryption;

/**
 * @author 陈汉辉
 * @since 1.0.0
 */
public class EncryptionServiceFactory {

    private final SecurityEncryptConfig config;

    public EncryptionServiceFactory(SecurityEncryptConfig config) {
        this.config = config;
    }

    public Encryption createEncryptionService() {
        switch (config.getMode()) {
            case RSA:
                return new RSAEncryption(config);
            case AES:
                return new AESEncryption(config);
            case SM4:
                return new SM4Encryption(config);
        }
        throw new RuntimeException("Unknown encryption mode: " + config.getMode());
    }
}
