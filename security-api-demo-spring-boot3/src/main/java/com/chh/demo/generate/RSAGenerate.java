package com.chh.demo.generate;

import com.chh.util.RSAUtils;

import java.util.Map;

/**
 * @author 陈汉辉
 */
public class RSAGenerate {

    public static void main(String[] args) {
        int bits = 2048;
        Map<String, String> keyMap = RSAUtils.generateKeyPair(bits);
        String publicKeyStr = keyMap.get("publicKey");
        String privateKeyStr = keyMap.get("privateKey");
        System.out.println("=======================================");
        System.out.println("bits：" + bits);
        System.out.println("publicKey：" + publicKeyStr);
        System.out.println("privateKey：" + privateKeyStr);
        System.out.println("publicKey length：" + publicKeyStr.length());
        System.out.println("privateKey length：" + privateKeyStr.length());
        System.out.println("=======================================");
    }

}

