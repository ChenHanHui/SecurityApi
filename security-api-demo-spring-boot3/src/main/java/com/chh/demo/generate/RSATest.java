package com.chh.demo.generate;

import com.chh.util.Base64Utils;
import com.chh.util.HashUtils;
import com.chh.util.RSAUtils;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.Map;

/**
 * @author 陈汉辉
 */
public class RSATest {
    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeySpecException {
        System.out.println("======================简单加密解密=========================");
        encryptAndDecrypt();
        System.out.println("==========================================================");

        System.out.println("======================数字签名和验签========================");
        signAndVerify();
        System.out.println("==========================================================");

        System.out.println("=============标准的RAS加密加签验签解密(两对密钥)==============");
        verifyAndDecrypt();
        System.out.println("==========================================================");

        System.out.println("=============加密数据进行数据摘要、签名(一对密钥)==============");
        isValidSignature();
        System.out.println("==========================================================");

        System.out.println("====================模拟客户端加密加签======================");
        clientEncryptSignature();
        System.out.println("==========================================================");
    }

    public static void encryptAndDecrypt() throws NoSuchAlgorithmException, InvalidKeySpecException {
        Map<String, String> keyMap = RSAUtils.generateKeyPair();
        String publicKeyStr = keyMap.get("publicKey");
        String privateKeyStr = keyMap.get("privateKey");
        System.out.println("-----------------生成的公钥和私钥------------------------------");
        System.out.println("获取到的公钥：" + publicKeyStr);
        System.out.println("获取到的私钥：" + privateKeyStr);
        PublicKey publicKey = RSAUtils.generatePublicKeyFromBase64(publicKeyStr);
        PrivateKey privateKey = RSAUtils.generatePrivateKeyFromBase64(privateKeyStr);
        // 待加密数据
        String data = "tranSeq=1920542585&amount=100&payType=wechat";
        // 公钥加密
        System.out.println("-----------------加密和解密------------------------------");
        System.out.println("待加密的数据：" + data);
        String encrypt = RSAUtils.encryptByPublicKey(data, publicKey);
        System.out.println("加密后数据：" + encrypt);
        // 私钥解密
        String decrypt = RSAUtils.decryptByPrivateKey(encrypt, privateKey);
        System.out.println("解密后数据：" + decrypt);
    }

    public static void signAndVerify() throws NoSuchAlgorithmException, InvalidKeySpecException {
        Map<String, String> keyMap = RSAUtils.generateKeyPair();
        String publicKeyStr = keyMap.get("publicKey");
        String privateKeyStr = keyMap.get("privateKey");
        System.out.println("-----------------生成的公钥和私钥------------------------------");
        System.out.println("获取到的公钥：" + publicKeyStr);
        System.out.println("获取到的私钥：" + privateKeyStr);
        PublicKey publicKey = RSAUtils.generatePublicKeyFromBase64(publicKeyStr);
        PrivateKey privateKey = RSAUtils.generatePrivateKeyFromBase64(privateKeyStr);
        // 数字签名
        String data = "tranSeq=1920542585&amount=100&payType=wechat";
        System.out.println("待签名的数据：" + data);
        String sign = RSAUtils.sign(data.getBytes(StandardCharsets.UTF_8), privateKey, "SHA256withRSA");
        System.out.println("数字签名结果：" + sign);
        boolean verify = RSAUtils.verify(data.getBytes(StandardCharsets.UTF_8), Base64Utils.decodeFromString(sign), publicKey, "SHA256withRSA");
        System.out.println("数字签名验证结果：" + verify);
    }

    public static void verifyAndDecrypt() throws NoSuchAlgorithmException, InvalidKeySpecException {
        System.out.println("-----------------服务器生成的公钥和私钥------------------------------");
        Map<String, String> serverkeyMap = RSAUtils.generateKeyPair();
        String serverPublicKeyStr = serverkeyMap.get("publicKey");
        String serverPrivateKeyStr = serverkeyMap.get("privateKey");
        System.out.println("服务器获取到的公钥：" + serverPublicKeyStr);
        System.out.println("服务器获取到的私钥：" + serverPrivateKeyStr);
        System.out.println("-----------------------------------------------------------------");
        System.out.println("-----------------客户端生成的公钥和私钥------------------------------");
        Map<String, String> clientkeyMap = RSAUtils.generateKeyPair();
        String clientPublicKeyStr = clientkeyMap.get("publicKey");
        String clientPrivateKeyStr = clientkeyMap.get("privateKey");
        System.out.println("客户端获取到的公钥：" + clientPublicKeyStr);
        System.out.println("客户端获取到的私钥：" + clientPrivateKeyStr);
        System.out.println("-----------------------------------------------------------------");
        PublicKey serverPublicKey = RSAUtils.generatePublicKeyFromBase64(serverPublicKeyStr);
        PrivateKey serverPrivateKey = RSAUtils.generatePrivateKeyFromBase64(serverPrivateKeyStr);
        PublicKey clientPublicKey = RSAUtils.generatePublicKeyFromBase64(clientPublicKeyStr);
        PrivateKey clientPrivateKey = RSAUtils.generatePrivateKeyFromBase64(clientPrivateKeyStr);
        // 客户端加密、数字签名，服务器验签、解密
        String data = "一碗情深";
        System.out.println("客户端原始数据：" + data);
        String encrypt = RSAUtils.encryptByPublicKey(data, serverPublicKey);
        System.out.println("客户端使用服务器公钥加密：" + encrypt);
        String sign = RSAUtils.sign(encrypt.getBytes(StandardCharsets.UTF_8), clientPrivateKey, "SHA256withRSA");
        System.out.println("客户端将加密数据用客户端私钥签名：" + sign);
        boolean verify = RSAUtils.verify(encrypt.getBytes(StandardCharsets.UTF_8), Base64Utils.decodeFromString(sign), clientPublicKey, "SHA256withRSA");
        System.out.println("服务器使用客户端公钥数字签名验证结果：" + verify);
        if (verify) {
            System.out.println("服务器使用客户端公钥数字签名验证通过，开始解密数据");
            String decrypt = RSAUtils.decryptByPrivateKey(encrypt, serverPrivateKey);
            System.out.println("服务器使用服务器私钥解密：" + decrypt);
        }
    }

    public static void isValidSignature() throws NoSuchAlgorithmException, InvalidKeySpecException {
        System.out.println("-----------------客户端生成的公钥和私钥------------------------------");
        Map<String, String> clientkeyMap = RSAUtils.generateKeyPair();
        String clientPublicKeyStr = clientkeyMap.get("publicKey");
        String clientPrivateKeyStr = clientkeyMap.get("privateKey");
        System.out.println("客户端获取到的公钥：" + clientPublicKeyStr);
        System.out.println("客户端获取到的私钥：" + clientPrivateKeyStr);
        System.out.println("-----------------------------------------------------------------");
        PublicKey clientPublicKey = RSAUtils.generatePublicKeyFromBase64(clientPublicKeyStr);
        PrivateKey clientPrivateKey = RSAUtils.generatePrivateKeyFromBase64(clientPrivateKeyStr);
        String data = "一碗情深";
        System.out.println("客户端原始数据：" + data);
        String encrypt = RSAUtils.encryptByPrivateKey(data, clientPrivateKey);
        System.out.println("客户端使用客户端私钥加密：" + encrypt);
        // 客户端使用SHA-256生成数据摘要
        String hash = HashUtils.computeHash(encrypt, "SHA-256");
        System.out.println("客户端对加密数据生成数据摘要：" + hash);
        // 客户端使用私钥对摘要进行签名
        String sign = RSAUtils.sign(hash.getBytes(StandardCharsets.UTF_8), clientPrivateKey, "SHA256withRSA");
        System.out.println("客户端使用客户端私钥对数据摘要进行签名，生成数字签名：" + sign);
        // 服务器使用客户端公钥对摘要进行验证签名，确保数据的完整性和来源的真实性
        boolean verify = RSAUtils.verify(hash.getBytes(StandardCharsets.UTF_8), Base64Utils.decodeFromString(sign), clientPublicKey, "SHA256withRSA");
        System.out.println("服务器使用客户端公钥验证签名结果：" + verify);
        if (verify) {
            System.out.println("数据摘要签名验证成功，数据有效");
            String decrypt = RSAUtils.decryptByPublicKey(encrypt, clientPublicKey);
            System.out.println("服务器使用客户端公钥对加密数据解密：" + decrypt);
        }
    }

    public static void clientEncryptSignature() throws NoSuchAlgorithmException, InvalidKeySpecException {
        String clientPublicKeyStr = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAhBIwiQSVRBtaSkjpw3tQcDPv9rPxZ4a5CCcwldCQ8mECx/pz3AtSJ7WtBmRmmVpzgfYxZijifFyRmpIACalDQBoCPPj49r3fVvgPhGY2GQ/p5QwSBsQxCMwXITxKJmFGcbO3m4OrWs7GV3z311wQ4roBydoviWIYDjVRLMIylMB0ISFL53hyFMsbO7jCK+fZf3S7BOe9/laaBbqYtpO3gjl9q/GZyqzkLgKqdWOxesC4dsfDDuzuBNCHJNQL4O9EbG+7DJjAz5rbwlv47QiuJF+0d6S2vbd4QwjgJ8l27pFDx/4hD6/4+9Lkv3HcI00hsgxDTl0k56Qtfa1L37zAEwIDAQAB";
        String serverPublicKeyStr = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAujK19EKJMp2uhcL4RKALo9QLizU7Rmv2bJRQeA7nk1FB3YvSjB4JWxXvfSdeECi2cC/O7t2OjTDHRXwzjHAszHRlg59Csysl/Bl+TPO0f8a5yAj+Rygy/rhgRZHYm3ggF+cLUAXPgIJK/0MWTf/kfAD/mfMIJyRwASClaAmn8d6AoeNaNZ7IS4nCSy/2lKF4QWA3rMYMyI1fqdNnty5Te2MjadfcElHCykjnj+K+VhM1n/LeHYw0ND/r+CiPSXehATm/8hrRdBb416PSOUJzeVMcDJI7CoYQV1Tj8xlW6PjoUY3czh9Tcxm596zsqqvwehXyr0SKjIZ8EBHft8W7OQIDAQAB";
        String clientPrivateKeyStr = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCEEjCJBJVEG1pKSOnDe1BwM+/2s/FnhrkIJzCV0JDyYQLH+nPcC1Inta0GZGaZWnOB9jFmKOJ8XJGakgAJqUNAGgI8+Pj2vd9W+A+EZjYZD+nlDBIGxDEIzBchPEomYUZxs7ebg6tazsZXfPfXXBDiugHJ2i+JYhgONVEswjKUwHQhIUvneHIUyxs7uMIr59l/dLsE573+VpoFupi2k7eCOX2r8ZnKrOQuAqp1Y7F6wLh2x8MO7O4E0Ick1Avg70Rsb7sMmMDPmtvCW/jtCK4kX7R3pLa9t3hDCOAnyXbukUPH/iEPr/j70uS/cdwjTSGyDENOXSTnpC19rUvfvMATAgMBAAECggEAQDRni/TCa92wQzS3D+U+8Y0ONyy7Sszf3urOX8aI8Ivgwj8rW+nVAqD7gIQGrL6gMKny0etGT5bg4ffgNNIoc10xgVq4DZu4m5r0UDUwT3bBCiyYBtoRzGkLRYj9szLPAC+SLvQPtZGYMqf6BKQpQU+T1wFO4sbhUubwk4XE+TLzd+tRjflftnJ+6T+eG5z85AJbFb2bnw4ZG8kr6doWphPVvKDxhqMk8H9gFKag8tXnDKES9wNcK+QEwAIRdUp/+Y5kiH9CqAkgcIlJd+MB0l/De+NSmRKb0WKKZVXb3kQ23lCQ5QLQFOeSXeCHv6ifBQIiY5UqH+X2MnkEVZwkAQKBgQC5nxTk86EugdxC7Rf+I7Z7uqY2Up6LAN3+5p1qmmLJ9B7R4uP5OyhX0VBzS+ew7sDRadsOM/1mctcF3+NcAEv0h0fO6kejygKsVYH9ZqVr/LEbuoKF5RMoc2L7I7pEpxACcCDzGntyvtnqfO6drEGQIbds46T2z1Grmcz/V3EagQKBgQC2JV1aPx9wL8LuEYIkEwgPwWfmkFcCOjw2YLHVN48QVHTDPomsMupPEqRDBY/BeagUSLzBhJZYjfSdS7L/VZDaa7WtiHYdMC66Y0q3N3qL8FK5TM/LYApiGWRMH7r6czcpeXzUDECZt7hptjpNy2lNH5KmGIHhftFp8R/ZvLaIkwKBgFPj7LqIzTjKofRxWR2XoFqNAZ+deV/TNYGFUnGicrFZLjCnfWbfcJ030Uc6b+0MCoLad5EUuBp8HmfeRtAGpk1Ocoq2YUhJ/SlLv+L5aBvE6Xphsam33Q+0DnD4Na/fXSHs8UdNmatDTBNpASKrfjoYsuB4hc6x5fRLH1XrfAeBAoGAUbBQPbkxkE7bTYnso+ElLGvA/VaerxEPEBQtrUJNlzVJaIcTl3b+PphNswE7h7nX98DnZqLgCrcfjav1iy4E2PCVek5JKq4BRvZg++qVu0z8zL32M2/POE/K+5j/cnNZufASW8di8Y3D1yUy4NDpWP1f973mAguuP6U0c6J++qECgYBjLN3isb3dJwZh2xXRYY37yOybiR4HoZGMYYAf7+qOO7eZpOUxxhvzezlm/2dMsu6Cd0LwVg82M12zls/PHK1I0GNn5/VZet7+npAq7t7wvE7Mxx3Q8WkD+PFCTpnVPDiJymcKNj0y3A2VqJFkKmbHWsBUbIj7Aak3/oMcjIChZA==";
        String serverPrivateKeyStr = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQC6MrX0Qokyna6FwvhEoAuj1AuLNTtGa/ZslFB4DueTUUHdi9KMHglbFe99J14QKLZwL87u3Y6NMMdFfDOMcCzMdGWDn0KzKyX8GX5M87R/xrnICP5HKDL+uGBFkdibeCAX5wtQBc+Agkr/QxZN/+R8AP+Z8wgnJHABIKVoCafx3oCh41o1nshLicJLL/aUoXhBYDesxgzIjV+p02e3LlN7YyNp19wSUcLKSOeP4r5WEzWf8t4djDQ0P+v4KI9Jd6EBOb/yGtF0FvjXo9I5QnN5UxwMkjsKhhBXVOPzGVbo+OhRjdzOH1NzGbn3rOyqq/B6FfKvRIqMhnwQEd+3xbs5AgMBAAECggEAHc4BPzSpVn9UENhJOl7uDg7ivClMsDSW8QF6sgUcv4GZy/dWQBSMxdZIjB2Rmn+R2Hpmoui3KVWeCDrd24SXPzEAkOWc3SYygdyo0XOPOIbcdS8vWL+QDiMAUhKK/DPp2zmiH3CWqwjwLVUVTG4CKnFUeNWkv/M+tId/wpwNFJOVISZgRPqeD2Lm65JaCA87h1fWLkT9CMqL1z5IzRXmpP9GCk8CHJfHUJO25JU+UxWwj1daCRrAVaOIFgesftoq+H4xvIzNegXObNajWSA+yYqV6RHAa5/HXLgtJlsPP/CkMaNgnhNEKbtUoGHP3g6AO/t9g1gJNUx1/nFeANA2OwKBgQD79kDkla651uMsXnY9ifXlvGhwU3JGQ23v1jH+KJgJiSydoqg7Ib5bL8HjePqmDuosPQfxK8Z9S4h8QHMjpNqbNodRtNY+Oka9pat38bPyiCnbpOcYLH1AL/O0HaMOrSevrNZgZbm2oez9RqZVCEIZrskPL/F54/HXW66RTGXgQwKBgQC9LqRjp8xal9EAuafO+FAL71HQJ+UQTtYXHVY5uu9E1hDqpV/c+x7eyqMkJdaDw+uiln7iV6Xh2U8uYIKjHEgnOFLWngmBxQzZgZ69e80fvN5Y/9qjDzngL8kZIEOxLczmDobLscR5Oa5O9UU0VFnyEJ+2cPMsDhGXiqNBez5M0wKBgH4r2F8fHXnuQ6BKRUeXuE0sxdfJ0aWhyoGZbztotInB6VeexkOmKB6dkagJQuq4+Ubh4QJB+4lus5AU7hNhUB79dVHTDp3mrNn3BiBwJr5CtUNKFncigC4OVSlS1f5Zc1Ajas6m4hK0j2pdYJmgIwmdXw194pw5Kze+r3IPIczlAoGADMPG58p8qWgw/AGK5xYuIIAHXyox9IW9QFYEaWTJnDVtcsexzjFLhfDaTSgYb0+a6J+K9C7BzJljFFgCuIrB+5N7U2vabF0lXs+2LEbcMlYGEq9Ay1vNpejTgtSAfMYAW02Dnt7hyOQxdLZr8vn2D7U7cY3xenmAil8aYOK1820CgYA1FJN1H1r9zdpvU1XYn+2G4IPgE/zT5xPyzWVB2TkIsPaNDCmznAVPsbi8Qau9sFQBp0rmDug8bVFdmHVpaQk7IxkprGI3XOrAZrvLI6gfVpLHdkLbwy4LHWyrYIM93EoJddL+iOln84R092pLc+Qz6jPBWpJvrzNrzUTUGhPIUg==";

        PublicKey serverPublicKey = RSAUtils.generatePublicKeyFromBase64(serverPublicKeyStr);
        PrivateKey serverPrivateKey = RSAUtils.generatePrivateKeyFromBase64(serverPrivateKeyStr);
        PublicKey clientPublicKey = RSAUtils.generatePublicKeyFromBase64(clientPublicKeyStr);
        PrivateKey clientPrivateKey = RSAUtils.generatePrivateKeyFromBase64(clientPrivateKeyStr);

        // 模拟客户端加密加签
        String data = "{\"name\":\"一碗情深\",\"age\":27,\"day\":\"2099-01-01 00:00:00\"}";
        System.out.println("客户端原始数据：" + data);
        String content = RSAUtils.encryptByPublicKey(data, serverPublicKey);
        System.out.println("客户端使用服务器公钥加密：" + content);
        String sign = RSAUtils.sign(content.getBytes(StandardCharsets.UTF_8), clientPrivateKey, "SHA256withRSA");
        System.out.println("客户端将加密数据用客户端私钥签名：" + sign);
    }

}
