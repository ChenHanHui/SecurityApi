package com.chh.demo.controller;

import com.chh.annotation.SecurityParameter;
import com.chh.constant.SecurityConstant;
import com.chh.demo.entity.Author;
import com.chh.util.SecurityBuilder;
import com.chh.util.SecurityResult;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author 陈汉辉
 */
@RestController
@RequestMapping("/author")
@SecurityParameter
public class AuthorController implements SecurityBuilder {

    /**
     * 请求不解密，响应加密(非@RequestBody参数不能解密，响应参数可以加密)
     *
     * @param author Author对象
     * @return 返回加密后的数据 ResponseBody<SecurityResult>格式
     */
    @GetMapping("/get")
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
        return ok(author);
    }

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
    public ResponseEntity<SecurityResult> originalData(@RequestBody @Validated Author author,
                               HttpServletRequest request, HttpServletResponse response) {
        System.out.println("请求参数解密前: " + request.getAttribute(SecurityConstant.INPUT_ORIGINAL_DATA));
        System.out.println("请求的签名数据: " + request.getAttribute(SecurityConstant.INPUT_ORIGINAL_SIGN));
        System.out.println("请求参数解密后: " + request.getAttribute(SecurityConstant.INPUT_DECRYPT_DATA));
        author.setUrl("https://blog.csdn.net/xiaohuihui1400");
        return success(author);
    }

}
