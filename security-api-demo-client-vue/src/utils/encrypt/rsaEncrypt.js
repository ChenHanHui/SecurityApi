import JSEncrypt from 'jsencrypt'
import sha256 from 'crypto-js/sha256'
import { splitStringByLength } from "@/utils/index"
import { generateHash } from "@/utils/encrypt/hashEncrypt"

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

const keySize = 2048

// 分段加密
export function encrypt(data) {
  const encryptor = new JSEncrypt()
  encryptor.setPublicKey(serverPublicKey) // 设置服务端公钥
  const blocks = splitStringByLength(data, Math.floor((keySize / 8 - 11) / 3))
  let encryptData = '';
  for (let i = 0; i < blocks.length; i++) {
    const encryptBlock = encryptor.encrypt(blocks[i])
    if (encryptBlock) {
      encryptData += encryptBlock + ';'
    } else {
      throw new Error("Encryption failed for segment.")
    }
  }
  return encryptData.slice(0, encryptData.length - 1)
}

// 分段解密
export function decrypt(data) {
  const encryptor = new JSEncrypt()
  encryptor.setPrivateKey(clientPrivateKey) // 设置客户端私钥
  const blocks = data.split(";")
  let decryptedData = '';
  for (let i = 0; i < blocks.length; i++) {
    const decryptedBlock = encryptor.decrypt(blocks[i])
    if (decryptedBlock) {
      decryptedData += decryptedBlock
    } else {
      throw new Error("Decryption failed for segment.")
    }
  }
  return decryptedData
}

// 签名
export async function sign(data) {
  const encryptor = new JSEncrypt()
  encryptor.setPrivateKey(clientPrivateKey) // 设置客户端私钥
  // 使用SHA-256哈希函数进行签名
  const hash = await generateHash(data)
  return encryptor.sign(hash, sha256, "sha256")
}

// 验签
export async function verify(data, signedTxt) {
  const encryptor = new JSEncrypt()
  encryptor.setPublicKey(serverPublicKey) // 设置服务端公钥
  // 使用SHA-256哈希函数进行签名
  const hash = await generateHash(data)
  // 对已签名的文本进行验签
  return encryptor.verify(hash, signedTxt, sha256)
}
