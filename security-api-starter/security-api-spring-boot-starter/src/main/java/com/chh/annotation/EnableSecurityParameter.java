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

import com.chh.advice.EncryptRequestBodyAdvice;
import com.chh.advice.EncryptResponseBodyAdvice;
import com.chh.config.properties.SecurityEncryptConfig;
import com.chh.handler.GlobSecurityExceptionHandler;
import com.chh.service.EncryptionService;
import com.chh.servlet.HttpServletRequestInputStreamFilter;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author 陈汉辉
 * @since 1.0.0
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Import({
        HttpServletRequestInputStreamFilter.class,
        SecurityEncryptConfig.class,
        EncryptResponseBodyAdvice.class,
        EncryptRequestBodyAdvice.class,
        GlobSecurityExceptionHandler.class,
        EncryptionService.class
})
public @interface EnableSecurityParameter {
}
