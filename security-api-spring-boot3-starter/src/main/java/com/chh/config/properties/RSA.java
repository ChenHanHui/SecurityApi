package com.chh.config.properties;

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

    public RSA() {
    }

    public String getPrivateKey() {
        return this.privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

}
