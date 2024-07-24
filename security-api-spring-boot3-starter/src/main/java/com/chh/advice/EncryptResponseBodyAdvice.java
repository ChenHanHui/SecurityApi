package com.chh.advice;

import com.chh.annotation.SecurityParameter;
import com.chh.config.properties.SecretEncryptConfig;
import com.chh.exception.SecurityException;
import com.chh.service.EncryptionService;
import com.chh.util.SecurityResult;
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
import com.chh.util.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

@ControllerAdvice
public class EncryptResponseBodyAdvice extends CommonAdvice implements ResponseBodyAdvice<SecurityResult> {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final SecretEncryptConfig secretEncryptConfig;
    private final EncryptionService encryptionService;
    private final ObjectMapper objectMapper;

    @Autowired
    public EncryptResponseBodyAdvice(SecretEncryptConfig secretEncryptConfig, EncryptionService encryptionService, ObjectMapper objectMapper) {
        this.secretEncryptConfig = secretEncryptConfig;
        this.encryptionService = encryptionService;
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> converterType) {
        SecurityParameter securityAnnotation = getSecurityParameter(methodParameter);
        if (securityAnnotation == null) {
            return false;
        }
        boolean isEncrypt = secretEncryptConfig.isOutEncode() && securityAnnotation.outEncode();
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
        if (returnType instanceof ParameterizedType parameterizedType) {
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
            String result = encryptionService.encrypt(dataString);
            SecurityParameter securityAnnotation = getSecurityParameter(methodParameter);
            boolean showLog = showLog(securityAnnotation, secretEncryptConfig);
            if (showLog) {
                log.info("Pre-encrypted data: {}, After encryption: {}", dataString, result);
            }
            return body.setData(result);
        } catch (JsonProcessingException e) {
            log.error("Response body serialization failed", e);
            throw new SecurityException("Response body serialization failed");
        } catch (Exception e) {
            log.error("Response body encryption failed", e);
            throw new SecurityException("Response body encryption failed");
        }
    }

}
