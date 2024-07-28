package com.chh.service;

import com.chh.config.properties.SecretEncryptConfig;
import com.chh.encryption.Encryption;
import com.chh.factory.EncryptionServiceFactory;
import com.chh.util.SecurityData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EncryptionService implements Encryption {

    private final Encryption encryption;

    @Autowired
    public EncryptionService(SecretEncryptConfig secretEncryptConfig) {
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
