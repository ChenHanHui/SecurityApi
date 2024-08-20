/*
 * Copyright 2024-2099 ChenHanHui
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.chh.annotation;

import org.springframework.web.bind.annotation.Mapping;

import java.lang.annotation.*;

/**
 * @author 陈汉辉
 * @since 1.0.0
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Mapping
@Documented
public @interface SecurityParameter {

    /**
     * 请求参数是否解密
     *
     * @return 默认为true
     */
    boolean inDecode() default true;

    /**
     * 响应参数是否加密
     *
     * @return 默认为true
     */
    boolean outEncode() default true;

    /**
     * 是否打印请求参数、响应参数解密加密前后等敏感数据
     *
     * @return 默认为true
     */
    boolean showLog() default true;

    /**
     * 是否缓存请求加密、签名数据，以及解密之后的数据到 request
     *
     * @return 默认为true
     */
    boolean cacheData() default true;

}
