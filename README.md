<p align="center">
	<img alt="logo" src="https://i-blog.csdnimg.cn/direct/686de8ae5a3f4c1d9d0b68280c5689a5.png" width="150" height="150">
</p>
<h1 align="center" style="margin: 30px 0 30px; font-weight: bold;">SecurityApi v1.0.1</h1>
<h4 align="center">一个基于 Java 接口参数加密框架，让接口参数加密变得简单、优雅！</h4>
<p align="center">
    <br />
        <a target="_blank" href="https://github.com/ChenHanHui/SecurityApi">
            <img src="https://img.shields.io/badge/GitHub-SecurityApi-yellow?style=flat-square&logo=GitHub" alt="GitHub" />
        </a>
        <a target="_blank" href="https://gitee.com/chen-hanhui/SecurityApi.git">
            <img src="https://img.shields.io/badge/Gitee-SecurityApi-yellow?style=flat-square&logo=Gitee" alt="Gitee" />
        </a>
        <a target="_blank" href="https://gitcode.com/xiaohuihui1400/SecurityApi.git">
            <img src="https://img.shields.io/badge/GitCode-SecurityApi-yellow?style=flat-square&logo=GitCode" alt="GitCode" />
        </a>
        <a target="_blank" href="https://mvnrepository.com/artifact/io.github.chenhanhui/security-api-spring-boot-starter">
            <img src="https://img.shields.io/badge/Maven%20Central-SpringBoot%20v2.x-blue?style=flat-square" alt="Maven" />
        </a>
        <a target="_blank" href="https://mvnrepository.com/artifact/io.github.chenhanhui/security-api-spring-boot3-starter">
            <img src="https://img.shields.io/badge/Maven%20Central-SpringBoot%20v3.x-blue?style=flat-square" alt="Maven" />
        </a>
    <br />
    <a target="_blank" href="https://github.com/ChenHanHui/SecurityApi/blob/master/LICENSE">
		<img src="https://img.shields.io/badge/license-Apache2-green?style=flat-square" alt="Apache 2" />
	</a>
</p>

---

### SecurityApi 介绍

SecurityApi 是一个基于 Java 接口参数加密框架，可以让请求参数解密，响应参数加密，目前支持AES、RSA加密模式，RSA采用分段加密的方式。

### SecurityApi 模块

- `security-api-demo-client-vue`：客户端 Vue3 + Element-Plus 演示示例
- `security-api-demo-spring-boot2`：SpringBoot2.x 版本服务器端演示示例
- `security-api-demo-spring-boot3`：SpringBoot3.x 版本服务器端演示示例
- `security-api-spring-boot-starter`：SpringBoot2.x 版本源码
- `security-api-spring-boot3-starter`：SpringBoot3.x 版本源码

### SecurityApi 依赖

```xml
<dependencies>
    <!-- SpringBoot2.x 依赖 -->
    <!-- https://mvnrepository.com/artifact/io.github.chenhanhui/security-api-spring-boot-starter -->
    <dependency>
        <groupId>io.github.chenhanhui</groupId>
        <artifactId>security-api-spring-boot-starter</artifactId>
        <version>1.0.1</version>
    </dependency>

    <!-- SpringBoot3.x 依赖 -->
    <!-- https://mvnrepository.com/artifact/io.github.chenhanhui/security-api-spring-boot3-starter -->
    <dependency>
        <groupId>io.github.chenhanhui</groupId>
        <artifactId>security-api-spring-boot3-starter</artifactId>
        <version>1.0.1</version>
    </dependency>
</dependencies>
```

<details>
<summary><b>简单示例展示：（点击展开 / 折叠）</b></summary>

在启动类中添加 `@EnableSecurityParameter` 注解启动 SecurityApi 功能：

```java
@SpringBootApplication
@EnableSecurityParameter
public class SecurityApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecurityApiApplication.class, args);
    }

}
```

如果使用RSA加密，需要在 `application.yml` 添加以下代码：

```yml
security:
  encrypt:
    mode: rsa
    rsa:
      private-key: 'MIIEvAIBADAN...PIUg=='
      client-public-key: 'MIIBIjAN...37zAEwIDAQAB'
```

注意：`private-key` 是服务器私钥，`client-public-key` 是客户端公钥，默认为 2048 位。

密钥是有两对，服务器公钥和私钥，客户端公钥和私钥。

公钥双方都会有（包括对方的），私钥只有自己拥有自己的，不会服务器有客户端私钥，或者客户端有服务器私钥。

1. 当客户端向服务器发送数据请求时：

客户端用服务器的公钥进行数据加密，用客户端的私钥进行签名。

2. 服务器接收数据后：

服务器用客户端的公钥进行验签，用服务器私钥进行数据的解密。

3. 当服务器响应客户端数据结果时：

服务器是用客户端的公钥进行数据加密，用服务器私钥进行签名。

4. 客户端接收数据后：

客户端就用服务器公钥进行验签，用客户端的私钥进行解密。

这一切不需要开发者关心，SecurityApi 框架已经帮你做好了。

我们只需使用以下代码生成 RSA 公钥和私钥，需要生成两对，分别是客户端公钥和私钥，服务器公钥和私钥：

```java
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
        System.out.println("=======================================");
    }

}
```

在 SecurityApi 中，一行代码解决参数解密加密，只需在类或者方法上添加 `@SecurityParameter` 注解, 如下：

```java
@RestController
@RequestMapping("/author")
@SecurityParameter
public class AuthorController implements SecurityBuilder {
    
    /**
     * 请求解密，响应加密
     *
     * @param author Author对象
     * @return 返回加密后的数据 ResponseBody<SecurityResult>格式
     */
    @PostMapping("/inDecodeOutEncode")
    public ResponseEntity<SecurityResult> inDecodeOutEncode(@RequestBody @Validated Author author) {
        author.setUrl("https://blog.csdn.net/xiaohuihui1400");
        return success(author);
    }
    
}
```
</details>

### 代码托管
- GitHub：[https://github.com/ChenHanHui/SecurityApi](https://github.com/ChenHanHui/SecurityApi)
- Gitee：[https://gitee.com/chen-hanhui/SecurityApi.git](https://gitee.com/chen-hanhui/SecurityApi.git)
- GitCode：[https://gitcode.com/xiaohuihui1400/SecurityApi.git](https://gitcode.com/xiaohuihui1400/SecurityApi.git)

### 交流群
QQ交流群：982597743 [点击加入](https://qm.qq.com/q/E6Qf8gUUFO)

加入群聊的好处：
- 第一时间收到框架更新通知
- 第一时间收到框架 bug 通知

### 更多信息

<p><a href="https://blog.csdn.net/xiaohuihui1400/article/details/140759490" target="_blank">详情文档：https://blog.csdn.net/xiaohuihui1400/article/details/140759490</a></p>
