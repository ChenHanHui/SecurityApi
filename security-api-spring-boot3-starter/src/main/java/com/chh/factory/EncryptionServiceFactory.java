package com.chh.factory;

import com.chh.config.properties.SecretEncryptConfig;
import com.chh.encryption.AESEncryption;
import com.chh.encryption.Encryption;
import com.chh.encryption.RSAEncryption;

public class EncryptionServiceFactory {

    private final SecretEncryptConfig config;

    public EncryptionServiceFactory(SecretEncryptConfig config) {
        this.config = config;
    }

    public Encryption createEncryptionService() {
        return switch (config.getMode()) {
            case RSA -> new RSAEncryption(config);
            case AES -> new AESEncryption(config);
        };
    }
}
