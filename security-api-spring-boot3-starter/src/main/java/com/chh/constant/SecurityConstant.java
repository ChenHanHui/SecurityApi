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
package com.chh.constant;

/**
 * @author 陈汉辉
 * @since 1.0.0
 */
public class SecurityConstant {

    /** 请求解密之前的数据存放key */
    public static final String INPUT_ORIGINAL_DATA = "inputOriginalData";
    /** 请求解密之前签名存放key */
    public static final String INPUT_ORIGINAL_SIGN = "inputOriginalSign";
    /** 请求解密之后的数据存放key */
    public static final String INPUT_DECRYPT_DATA = "inputDecryptData";
    /** 客户端的RSA公钥 */
    public static final String CLIENT_PUBLIC_KEY = "clientPublicKey";

}
