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
package com.chh.advice;

import com.chh.annotation.SecurityParameter;
import com.chh.config.properties.SecretEncryptConfig;
import org.springframework.core.MethodParameter;

/**
 * @author 陈汉辉
 * @since 1.0.0
 */
abstract class CommonAdvice {

    protected SecurityParameter getSecurityParameter(MethodParameter methodParameter) {
        SecurityParameter securityAnnotation = methodParameter.getMethodAnnotation(SecurityParameter.class);
        if (securityAnnotation == null) {
            securityAnnotation = methodParameter.getContainingClass().getAnnotation(SecurityParameter.class);
        }
        return securityAnnotation;
    }

    protected boolean showLog(SecurityParameter securityAnnotation, SecretEncryptConfig secretEncryptConfig) {
        return securityAnnotation.showLog() && secretEncryptConfig.isShowLog();
    }

    protected boolean cacheData(SecurityParameter securityAnnotation, SecretEncryptConfig secretEncryptConfig) {
        return securityAnnotation.cacheData() && secretEncryptConfig.isCacheData();
    }

}
