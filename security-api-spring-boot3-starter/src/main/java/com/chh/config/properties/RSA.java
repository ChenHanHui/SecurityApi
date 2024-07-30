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
package com.chh.config.properties;

/**
 * @author 陈汉辉
 * @since 1.0.0
 */
public class RSA {

    // 密钥是有两对，服务器公钥和私钥，客户端公钥和私钥
    // 公钥双方都会有（包括对方的），私钥只有自己拥有自己的，不会服务器有客户端私钥，或者客户端有服务器私钥

    // 当客户端向服务器发送数据请求时：
    // 客户端是用服务器的公钥进行数据加密，用客户端的私钥进行签名。
    // 服务器接收数据后
    // 服务器用客户端的公钥进行验签，用服务器私钥进行数据的解密。

    // 当服务器响应客户端数据结果时：
    // 服务器是用客户端的公钥进行数据加密，用服务器私钥进行签名。
    // 客户端接收数据后
    // 客户端就用服务器公钥进行验签，用客户端的私钥进行解密。

    private String privateKey;
    private String clientPublicKey;
    private boolean sign = true;

    public RSA() {
    }

    public String getPrivateKey() {
        return this.privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getClientPublicKey() {
        return clientPublicKey;
    }

    public void setClientPublicKey(String clientPublicKey) {
        this.clientPublicKey = clientPublicKey;
    }

    public boolean getSign() {
        return sign;
    }

    public void setSign(boolean sign) {
        this.sign = sign;
    }

}
