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
import com.chh.config.properties.SecurityEncryptConfig;
import com.chh.exception.SecurityException;
import com.chh.service.EncryptionService;
import com.chh.util.SecurityData;
import com.chh.util.SecurityResult;
import com.chh.util.StringUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author 陈汉辉
 * @since 1.0.0
 */
@ControllerAdvice
public class EncryptResponseBodyAdvice extends CommonAdvice implements ResponseBodyAdvice<SecurityResult> {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final SecurityEncryptConfig securityEncryptConfig;
    private final EncryptionService encryptionService;
    private final ObjectMapper objectMapper;

    @Autowired
    public EncryptResponseBodyAdvice(SecurityEncryptConfig securityEncryptConfig, EncryptionService encryptionService, ObjectMapper objectMapper) {
        this.securityEncryptConfig = securityEncryptConfig;
        this.encryptionService = encryptionService;
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> converterType) {
        SecurityParameter securityAnnotation = getSecurityParameter(methodParameter);
        if (securityAnnotation == null) {
            return false;
        }
        boolean isEncrypt = securityEncryptConfig.isOutEncode() && securityAnnotation.outEncode();
        if (!isEncrypt) {
            return false;
        }

        // 获取方法的返回类型
        Type returnType = methodParameter.getGenericParameterType();

        // 检查是否直接返回SecurityResult
        if (returnType instanceof Class<?> && SecurityResult.class.isAssignableFrom((Class<?>) returnType)) {
            return true;
        }

        // 检查是否返回ResponseEntity<SecurityResult>
        if (returnType instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) returnType;
            Type rawType = parameterizedType.getRawType();
            // 检查原始类型是否为ResponseEntity
            if (rawType instanceof Class<?> && ResponseEntity.class.isAssignableFrom((Class<?>) rawType)) {
                Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                // 检查泛型参数是否为SecurityResult
                if (actualTypeArguments.length == 1 && actualTypeArguments[0] instanceof Class<?> && SecurityResult.class.isAssignableFrom((Class<?>) actualTypeArguments[0])) {
                    return true;
                }
            }
        }

        String className = methodParameter.getContainingClass().getName();
        Method method = methodParameter.getMethod();
        String message;
        if (method == null) {
            message = StringUtils.format(
                    "The return type of the method is not SecurityResult or ResponseEntity<SecurityResult>. Check: {}",
                    className
            );
            log.error(message);
            throw new SecurityException(message);
        }
        String methodName = method.getName();
        message = StringUtils.format(
                "The return type of the method is not SecurityResult or ResponseEntity<SecurityResult>. Check: {}.{}",
                className,
                methodName
        );
        log.error(message);
        throw new SecurityException(message);
    }

    @Override
    public SecurityResult beforeBodyWrite(SecurityResult body, MethodParameter methodParameter, MediaType mediaType,
                                          Class<? extends HttpMessageConverter<?>> converterType,
                                          ServerHttpRequest servletRequest, ServerHttpResponse serverHttpResponse) {
        Object data = null;
        if (body != null) {
            data = body.getData();
            body.setEncryption(true);
        }
        if (data == null) {
            return body;
        }
        try {
            String dataString = objectMapper.writeValueAsString(data);
            if (StringUtils.isBlank(dataString)) {
                return body;
            }
            SecurityData result = encryptionService.encrypt(dataString);
            SecurityParameter securityAnnotation = getSecurityParameter(methodParameter);
            boolean showLog = showLog(securityAnnotation, securityEncryptConfig);
            if (showLog) {
                if (result.getSign() != null) {
                    log.info("Pre-encrypted data: {}\nAfter encryption: {}\nsign: {}", dataString, result.getContent(), result.getSign());
                } else {
                    log.info("Pre-encrypted data: {}\nAfter encryption: {}", dataString, result.getContent());
                }
            }
            body.setData(result.getContent());
            if (result.getSign() != null) {
                body.setSign(result.getSign());
            }
            return body;
        } catch (JsonProcessingException e) {
            log.error("Response body serialization failed", e);
            throw new SecurityException("Response body serialization failed");
        } catch (Exception e) {
            log.error("Response body encryption failed", e);
            throw new SecurityException("Response body encryption failed");
        }
    }

}
