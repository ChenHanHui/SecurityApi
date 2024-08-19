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
package com.chh.demo.generate;

import com.chh.util.SM4Utils;

/**
 * @author 陈汉辉
 */
public class SM4Generate {

    public static void main(String[] args) throws Exception {
        String key = SM4Utils.generateKey();
        System.out.println("Generated Key: " + key);

        String iv = SM4Utils.generateIv();
        System.out.println("Generated IV: " + iv);

        String data = "Hello, this is a test message.";

        String encryptedData = SM4Utils.encrypt(data, key, iv);
        System.out.println("Encrypted data: " + encryptedData);

        String decryptedData = SM4Utils.decrypt(encryptedData, key, iv);
        System.out.println("Decrypted data: " + decryptedData);

        String encryptedDataNotIv = SM4Utils.encrypt(data, key, null);
        System.out.println("Encrypted data not iv: " + encryptedDataNotIv);

        String decryptedDataNotIv = SM4Utils.decrypt(encryptedDataNotIv, key, null);
        System.out.println("Decrypted data not iv: " + decryptedDataNotIv);
    }

}
