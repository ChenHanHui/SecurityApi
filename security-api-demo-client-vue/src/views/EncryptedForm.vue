<template>
  <div>
    <input v-model="formData.name" placeholder="Enter name" />
    <button @click="submitForm">Submit</button>
  </div>
</template>
<script>
import JSEncrypt from 'jsencrypt';
import axios from 'axios';
export default {
  data() {
    return {
      formData: {
        name: '',
        age: '',
        day: ''
      },
      publicKey: `-----BEGIN PUBLIC KEY-----
你的公钥
-----END PUBLIC KEY-----`,
    };
  },
  methods: {
    encryptData(data) {
      const encryptor = new JSEncrypt();
      encryptor.setPublicKey(this.publicKey);
      return window.btoa(encryptor.encrypt(JSON.stringify(data))); // 加密后进行Base64编码
    },
    async submitForm() {
      const encryptedData = this.encryptData(this.formData);
      try {
        const response = await axios.post('https://your-backend.com/api/submit', {
          data: encryptedData,
        }, {
          headers: {
            'Content-Type': 'application/json',
          },
        });
        // 假设响应也是加密且经过Base64编码的
        const decryptedResponse = this.decryptData(window.atob(response.data.data)); // Base64解码后解密
        console.log(decryptedResponse);
      } catch (error) {
        console.error("Error submitting form:", error);
      }
    },
    decryptData(data) {
      // 这里是解密响应数据的函数，假设你有私钥
      // 在实际应用中，响应数据的解密应该在服务器端进行
    },
  },
};
</script>