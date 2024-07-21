package com.chh.encryption;

import com.chh.util.SecurityData;

public interface Encryption {

    /**
     * 加密
     */
    String encrypt(String plainText);

    /**
     * 解密
     */
    String decrypt(SecurityData securityData);

}
