package com.chh.encryption;

import com.chh.util.SecurityData;

public interface Encryption {

    /**
     * 加密
     *
     * @param plainText 明文
     * @return 密文
     */
    SecurityData encrypt(String plainText);

    /**
     * 解密
     *
     * @param securityData 密文
     * @return 明文
     */
    String decrypt(SecurityData securityData);

}
