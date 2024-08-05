// 生成哈希摘要
export async function generateHash(data, algorithm = 'SHA-256') {
  // 使用TextEncoder将字符串密文转换为Uint8Array
  const encoder = new TextEncoder();
  const dataEncode = encoder.encode(data);
  const hashBuffer = await crypto.subtle.digest(algorithm, dataEncode)
  // 将得到的ArrayBuffer转换为Base64字符串
  const hashArray = new Uint8Array(hashBuffer);
  return btoa(String.fromCharCode(...hashArray));
}
