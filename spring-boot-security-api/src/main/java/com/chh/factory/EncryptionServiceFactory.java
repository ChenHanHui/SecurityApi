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
        switch (config.getMode()) {
            case RSA:
                return new RSAEncryption(config);
            case AES:
                return new AESEncryption(config);
        }
        throw new RuntimeException("Unknown encryption mode: " + config.getMode());
    }
}
