package com.chh.demo.generate;

import com.chh.util.AESUtils;

/**
 * @author 陈汉辉
 */
public class AESGenerate {

    public static void main(String[] args) throws Exception {
        int bit = 256;
        String key = AESUtils.generateKey(bit);
        String iv = AESUtils.generateIv();
        System.out.println("AES Key: " + key);
        System.out.println("AES Iv: " + iv);
        System.out.println("'123456' encrypt: " + AESUtils.encrypt("123456", key, iv));
        System.out.println("'123456' encrypt not iv: " + AESUtils.encrypt("123456", key));
    }

}
