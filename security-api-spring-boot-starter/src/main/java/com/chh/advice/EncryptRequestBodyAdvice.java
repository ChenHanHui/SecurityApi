package com.chh.advice;

import com.chh.annotation.SecurityParameter;
import com.chh.config.properties.SecretEncryptConfig;
import com.chh.constant.SecurityConstant;
import com.chh.exception.SecurityBadException;
import com.chh.exception.SecurityException;
import com.chh.service.EncryptionService;
import com.chh.util.SecurityData;
import com.chh.util.ServletUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import com.chh.util.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;

@ControllerAdvice
public class EncryptRequestBodyAdvice extends CommonAdvice implements RequestBodyAdvice{

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final SecretEncryptConfig secretEncryptConfig;
    private final EncryptionService encryptionService;
    private final ObjectMapper objectMapper;

    @Autowired
    public EncryptRequestBodyAdvice(SecretEncryptConfig secretEncryptConfig, EncryptionService encryptionService, ObjectMapper objectMapper) {
        this.secretEncryptConfig = secretEncryptConfig;
        this.encryptionService = encryptionService;
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean supports(MethodParameter methodParameter,
                            Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter methodParameter,
                                           Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return inputMessage;
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter methodParameter,
                                Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        SecurityParameter securityAnnotation = getSecurityParameter(methodParameter);
        if (securityAnnotation == null) {
            return body;
        }
        boolean encrypt = secretEncryptConfig.isInDecode() && securityAnnotation.inDecode();
        if (!encrypt) {
            return body;
        }

        // 获取request
        HttpServletRequest request = ServletUtils.getRequest();
        // Object decryptedData = request.getAttribute(SecurityConstant.INPUT_DECRYPT_DATA);
        // if (decryptedData != null) {
        //     // 如果之前已经解密过，则直接使用缓存的数据
        //     if (showLog) {
        //         log.warn("Request body has been decrypted before: {}", decryptedData);
        //     }
        //     return objectMapper.convertValue(decryptedData, body.getClass());
        // }

        try {
            // 获取数据
            SecurityData securityData = objectMapper.readValue(request.getInputStream(), SecurityData.class);
            if (securityData == null || StringUtils.isBlank(securityData.getContent())) {
                throw new SecurityBadException("Request body is no parameter 'content'");
            }
            String encryptedContent = securityData.getContent();
            // 缓存原始数据
            request.setAttribute(SecurityConstant.INPUT_ORIGINAL_DATA, encryptedContent);
            // 解密
            String decryptText = encryptionService.decrypt(securityData);
            // 缓存解密后的数据
            request.setAttribute(SecurityConstant.INPUT_DECRYPT_DATA, decryptText);

            boolean showLog = showLog(securityAnnotation, secretEncryptConfig);
            if (showLog) {
                log.info("Encrypted content received: {}, After decryption: {}", encryptedContent, decryptText);
            }

            // 解析解密后的数据
            return objectMapper.readValue(decryptText, body.getClass());
        } catch (SecurityBadException e) {
            throw new SecurityBadException(e.getMessage());
        } catch (JsonProcessingException e) {
            log.error("Request body serialization failed", e);
            throw new SecurityException("Request body serialization failed");
        } catch (UnsupportedEncodingException e) {
            log.error("Request body for unsupported encoding", e);
            throw new SecurityException("Request body for unsupported encoding");
        } catch (Exception e) {
            log.error("Request body decryption failed", e);
            throw new SecurityException("Request body decryption failed");
        }
    }

    @Override
    public Object handleEmptyBody(Object body, HttpInputMessage inputMessage, MethodParameter parameter,
                                  Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }

}
