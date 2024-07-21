package com.chh.annotation;

import org.springframework.web.bind.annotation.Mapping;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Mapping
@Documented
public @interface SecurityParameter {

    /**
     * 请求参数是否解密，默认解密
     */
    boolean inDecode() default true;

    /**
     * 响应参数是否加密，默认加密
     */
    boolean outEncode() default true;

    /**
     * 打印请求参数、响应参数解密加密前后的数据，以及密钥，默认打印
     */
    boolean showLog() default true;

}
