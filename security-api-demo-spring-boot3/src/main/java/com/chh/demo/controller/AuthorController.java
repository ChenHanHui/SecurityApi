package com.chh.demo.controller;

import com.chh.annotation.SecurityParameter;
import com.chh.constant.SecurityConstant;
import com.chh.demo.entity.Author;
import com.chh.util.SecurityBuilder;
import com.chh.util.SecurityResult;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author 陈汉辉
 */
@RestController
@RequestMapping("/author")
public class AuthorController implements SecurityBuilder {

    @ModelAttribute
    public void addAttributes(HttpServletRequest request) {
        String clientPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAhBIwiQSVRBtaSkjpw3tQcDPv9rPxZ4a5CCcwldCQ8mECx/pz3AtSJ7WtBmRmmVpzgfYxZijifFyRmpIACalDQBoCPPj49r3fVvgPhGY2GQ/p5QwSBsQxCMwXITxKJmFGcbO3m4OrWs7GV3z311wQ4roBydoviWIYDjVRLMIylMB0ISFL53hyFMsbO7jCK+fZf3S7BOe9/laaBbqYtpO3gjl9q/GZyqzkLgKqdWOxesC4dsfDDuzuBNCHJNQL4O9EbG+7DJjAz5rbwlv47QiuJF+0d6S2vbd4QwjgJ8l27pFDx/4hD6/4+9Lkv3HcI00hsgxDTl0k56Qtfa1L37zAEwIDAQAB";
        request.setAttribute(SecurityConstant.CLIENT_PUBLIC_KEY, clientPublicKey);
    }

    /**
     * 请求不解密，响应加密(非@RequestBody参数不能加密，响应参数可以加密)
     *
     * @param author Author对象
     * @return 返回加密后的数据 ResponseBody<SecurityResult>格式
     */
    @GetMapping("/get")
    @SecurityParameter
    public ResponseEntity<SecurityResult> get(@Validated Author author) {
        author.setUrl("https://blog.csdn.net/xiaohuihui1400");
        return success(author);
    }

    /**
     * 请求不解密，响应加密
     *
     * @param author Author对象
     * @return 返回加密后的数据 SecurityResult格式
     */
    @PostMapping("/outEncode")
    @SecurityParameter(inDecode = false)
    public SecurityResult outEncode(@RequestBody @Validated Author author) {
        author.setUrl("https://blog.csdn.net/xiaohuihui1400");
        return success(author).getBody();
    }

    /**
     * 请求解密，响应加密
     *
     * @param author Author对象
     * @return 返回加密后的数据 ResponseBody<SecurityResult>格式
     */
    @PostMapping("/inDecodeOutEncode")
    @SecurityParameter
    public ResponseEntity<SecurityResult> inDecodeOutEncode(@RequestBody @Validated Author author) {
        author.setUrl("https://blog.csdn.net/xiaohuihui1400");
        return success(author);
    }

    /**
     * 请求解密，响应不加密
     *
     * @param author Author对象
     * @return 返回解密后的数据 ResponseBody<SecurityResult>格式
     */
    @PostMapping("/inDecode")
    @SecurityParameter(outEncode = false)
    public ResponseEntity<SecurityResult> inDecode(@RequestBody @Validated Author author) {
        author.setUrl("https://blog.csdn.net/xiaohuihui1400");
        return success(author);
    }

    /**
     * 关闭加密解密参数日志
     *
     * @param author Author对象
     * @return 返回解密后的数据 ResponseBody<SecurityResult>格式
     */
    @PostMapping("/closeLog")
    @SecurityParameter(showLog = false)
    public ResponseEntity<SecurityResult> closeLog(@RequestBody @Validated Author author) {
        author.setUrl("https://blog.csdn.net/xiaohuihui1400");
        return success(author);
    }

    /**
     * 获取请求参数解密前后数据
     *
     * @param author Author对象
     * @return 返回解密后的数据 ResponseEntity<SecurityResult>格式
     */
    @PostMapping("/originalData")
    @SecurityParameter
    public ResponseEntity<SecurityResult> originalData(@RequestBody @Validated Author author,
                               HttpServletRequest request, HttpServletResponse response) {
        System.out.println("请求参数解密前: " + request.getAttribute(SecurityConstant.INPUT_ORIGINAL_DATA));
        System.out.println("请求参数解密后: " + request.getAttribute(SecurityConstant.INPUT_DECRYPT_DATA));
        author.setUrl("https://blog.csdn.net/xiaohuihui1400");
        return success(author);
    }

}
