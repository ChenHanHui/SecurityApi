package com.chh.annotation;

import org.springframework.web.bind.annotation.Mapping;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Mapping
@Documented
public @interface SecurityParameter {

    /**
     * 请求参数是否解密
     *
     * @return 默认为true，表示对请求参数解密
     */
    boolean inDecode() default true;

    /**
     * 响应参数是否加密
     *
     * @return 默认为true，表示对响应参数加密
     */
    boolean outEncode() default true;

    /**
     * 是否打印请求参数、响应参数解密加密前后等敏感数据
     *
     * @return 默认为true，表示打印请求参数、响应参数解密加密前后等敏感数据
     */
    boolean showLog() default true;

}
