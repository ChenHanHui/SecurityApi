import JSEncrypt from 'jsencrypt'

// 密钥对生成 http://web.chacuo.net/netrsakeypair

const serverPublicKey = 'MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAujK19EKJMp2uhcL4RKALo\n' +
  '9QLizU7Rmv2bJRQeA7nk1FB3YvSjB4JWxXvfSdeECi2cC/O7t2OjTDHRXwzjHAsz\n' +
  'HRlg59Csysl/Bl+TPO0f8a5yAj+Rygy/rhgRZHYm3ggF+cLUAXPgIJK/0MWTf/kf\n' +
  'AD/mfMIJyRwASClaAmn8d6AoeNaNZ7IS4nCSy/2lKF4QWA3rMYMyI1fqdNnty5Te\n' +
  '2MjadfcElHCykjnj+K+VhM1n/LeHYw0ND/r+CiPSXehATm/8hrRdBb416PSOUJze\n' +
  'VMcDJI7CoYQV1Tj8xlW6PjoUY3czh9Tcxm596zsqqvwehXyr0SKjIZ8EBHft8W7O\n' +
  'QIDAQAB'

const clientPrivateKey = 'MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCEEjCJBJVEG1pK\n' +
  'SOnDe1BwM+/2s/FnhrkIJzCV0JDyYQLH+nPcC1Inta0GZGaZWnOB9jFmKOJ8XJGa\n' +
  'kgAJqUNAGgI8+Pj2vd9W+A+EZjYZD+nlDBIGxDEIzBchPEomYUZxs7ebg6tazsZX\n' +
  'fPfXXBDiugHJ2i+JYhgONVEswjKUwHQhIUvneHIUyxs7uMIr59l/dLsE573+VpoF\n' +
  'upi2k7eCOX2r8ZnKrOQuAqp1Y7F6wLh2x8MO7O4E0Ick1Avg70Rsb7sMmMDPmtvC\n' +
  'W/jtCK4kX7R3pLa9t3hDCOAnyXbukUPH/iEPr/j70uS/cdwjTSGyDENOXSTnpC19\n' +
  'rUvfvMATAgMBAAECggEAQDRni/TCa92wQzS3D+U+8Y0ONyy7Sszf3urOX8aI8Ivg\n' +
  'wj8rW+nVAqD7gIQGrL6gMKny0etGT5bg4ffgNNIoc10xgVq4DZu4m5r0UDUwT3bB\n' +
  'CiyYBtoRzGkLRYj9szLPAC+SLvQPtZGYMqf6BKQpQU+T1wFO4sbhUubwk4XE+TLz\n' +
  'd+tRjflftnJ+6T+eG5z85AJbFb2bnw4ZG8kr6doWphPVvKDxhqMk8H9gFKag8tXn\n' +
  'DKES9wNcK+QEwAIRdUp/+Y5kiH9CqAkgcIlJd+MB0l/De+NSmRKb0WKKZVXb3kQ2\n' +
  '3lCQ5QLQFOeSXeCHv6ifBQIiY5UqH+X2MnkEVZwkAQKBgQC5nxTk86EugdxC7Rf+\n' +
  'I7Z7uqY2Up6LAN3+5p1qmmLJ9B7R4uP5OyhX0VBzS+ew7sDRadsOM/1mctcF3+Nc\n' +
  'AEv0h0fO6kejygKsVYH9ZqVr/LEbuoKF5RMoc2L7I7pEpxACcCDzGntyvtnqfO6d\n' +
  'rEGQIbds46T2z1Grmcz/V3EagQKBgQC2JV1aPx9wL8LuEYIkEwgPwWfmkFcCOjw2\n' +
  'YLHVN48QVHTDPomsMupPEqRDBY/BeagUSLzBhJZYjfSdS7L/VZDaa7WtiHYdMC66\n' +
  'Y0q3N3qL8FK5TM/LYApiGWRMH7r6czcpeXzUDECZt7hptjpNy2lNH5KmGIHhftFp\n' +
  '8R/ZvLaIkwKBgFPj7LqIzTjKofRxWR2XoFqNAZ+deV/TNYGFUnGicrFZLjCnfWbf\n' +
  'cJ030Uc6b+0MCoLad5EUuBp8HmfeRtAGpk1Ocoq2YUhJ/SlLv+L5aBvE6Xphsam3\n' +
  '3Q+0DnD4Na/fXSHs8UdNmatDTBNpASKrfjoYsuB4hc6x5fRLH1XrfAeBAoGAUbBQ\n' +
  'PbkxkE7bTYnso+ElLGvA/VaerxEPEBQtrUJNlzVJaIcTl3b+PphNswE7h7nX98Dn\n' +
  'ZqLgCrcfjav1iy4E2PCVek5JKq4BRvZg++qVu0z8zL32M2/POE/K+5j/cnNZufAS\n' +
  'W8di8Y3D1yUy4NDpWP1f973mAguuP6U0c6J++qECgYBjLN3isb3dJwZh2xXRYY37\n' +
  'yOybiR4HoZGMYYAf7+qOO7eZpOUxxhvzezlm/2dMsu6Cd0LwVg82M12zls/PHK1I\n' +
  '0GNn5/VZet7+npAq7t7wvE7Mxx3Q8WkD+PFCTpnVPDiJymcKNj0y3A2VqJFkKmbH\n' +
  'WsBUbIj7Aak3/oMcjIChZA=='

// 加密
export function encrypt(plainText) {
  const encryptor = new JSEncrypt()
  encryptor.setPublicKey(serverPublicKey) // 设置服务端公钥
  return encryptor.encrypt(plainText) // 对需要加密的数据进行加密
}

// 解密
export function decrypt(encryptedText) {
  const encryptor = new JSEncrypt()
  encryptor.setPrivateKey(clientPrivateKey) // 设置客户端私钥
  return encryptor.decrypt(encryptedText) // 对已加密的文本进行解密
}

// 签名
export async function sign(encryptedText) {
  const encryptor = new JSEncrypt()
  encryptor.setPrivateKey(clientPrivateKey) // 设置客户端私钥
  // 使用SHA-256哈希函数进行签名
  // const signature = encryptor.sign(data, crypto.subtle.digest, "sha256")
  return await encryptor.sign(encryptedText, async str => {
    return await generateHash(str)
  }, "sha256")
}

// 验签
export async function verify(encryptedText, signedTxt) {
  const encryptor = new JSEncrypt()
  encryptor.setPublicKey(serverPublicKey) // 设置服务端公钥
  // 对已签名的文本进行验签
  return encryptor.verify(encryptedText, signedTxt, async str => {
    return await generateHash(str)
  })
}

// 生成哈希
export async function generateHash(encryptedText) {
  // 使用TextEncoder将字符串密文转换为Uint8Array
  const encoder = new TextEncoder();
  const data = encoder.encode(encryptedText);
  const hashBuffer = await crypto.subtle.digest('SHA-256', data)
  // 将得到的ArrayBuffer转换为Base64字符串
  const hashArray = new Uint8Array(hashBuffer);
  const base64String = btoa(String.fromCharCode(...hashArray));
  return base64String;
  // const hashArray = Array.from(new Uint8Array(hashBuffer)); // 转换为字节数组
  // const hashHex = hashArray.map(b => b.toString(16).padStart(2, '0')).join(''); // 转换为十六进制字符串
  // return hashHex; // 返回十六进制字符串
}


// import CryptoJS from "crypto-js";
// import JSEncrypt from "jsencrypt";
// import JsRsaSign from "jsrsasign";
//  
// /**
//  * RSA加密
//  * @param publicKey 公钥
//  * @param plainText 明文
//  * @returns {*} 密文
//  */
// export function encryptByRSA(publicKey, plainText) {
//   const encryptor = new JSEncrypt();
//   encryptor.setPublicKey(publicKey);
//   return encryptor.encrypt(plainText);
// }
//  
// /**
//  * RSA解密
//  * @param privateKey 私钥
//  * @param cipherText 密文
//  * @returns {*} 明文
//  */
// export function decryptByRSA(privateKey, cipherText) {
//   const decrypter = new JSEncrypt();
//   decrypter.setPrivateKey(privateKey);
//   return decrypter.decrypt(cipherText);
// }
//  
// /**
//  * 生成RSA密钥对，填充模式为PKCS8。
//  * 更多模式参考：<a href="https://kjur.github.io/jsrsasign/api/symbols/KEYUTIL.html">https://kjur.github.io/jsrsasign/api/symbols/KEYUTIL.html</a>
//  * @returns {{privateKey: (string|string|*), publicKey: (string|string|*)}}
//  */
// export function generateRsaKeyWithPKCS8() {
//   const keyPair = JsRsaSign.KEYUTIL.generateKeypair("RSA", 1024);
//   const privateKey = JsRsaSign.KEYUTIL.getPEM(keyPair.prvKeyObj, "PKCS8PRV");
//   const publicKey = JsRsaSign.KEYUTIL.getPEM(keyPair.pubKeyObj);
//   return { privateKey, publicKey };
// }
//  
// /**
//  * SHA256和RSA加签
//  * @param privateKey 私钥
//  * @param msg 加签内容
//  * @returns {string} Base64编码签名内容
//  */
// export function signBySHA256WithRSA(privateKey, msg) {
//   const key = JsRsaSign.KEYUTIL.getKey(privateKey);
//   const signature = new JsRsaSign.KJUR.crypto.Signature({
//     alg: "SHA256withRSA",
//   });
//   signature.init(key);
//   signature.updateString(msg);
//   // 签名后的为16进制字符串，这里转换为16进制字符串
//   return JsRsaSign.hextob64(signature.sign());
// }
//  
// /**
//  * SHA256和RSA验签
//  * @param publicKey 公钥：必须为标准pem格式。如果是PKCS1格式，必须包含-----BEGIN RSA PRIVATE KEY-----，如果是PKCS8格式，必须包含-----BEGIN PRIVATE KEY-----
//  * @param base64SignStr Base64编码签名字符串
//  * @param msg 原内容
//  * @returns {boolean} 是否验签通过
//  */
// export function verifyBySHA256WithRSA(publicKey, base64SignStr, msg) {
//   const key = JsRsaSign.KEYUTIL.getKey(publicKey);
//   const signature = new JsRsaSign.KJUR.crypto.Signature({
//     alg: "SHA256withRSA",
//   });
//   signature.init(key);
//   signature.updateString(msg);
//   // 需要将Base64进制签名字符串转换成16进制字符串
//   return signature.verify(JsRsaSign.b64tohex(base64SignStr));
// }