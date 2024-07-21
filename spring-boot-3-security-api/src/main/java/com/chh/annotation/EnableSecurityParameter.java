package com.chh.annotation;

import com.chh.advice.EncryptRequestBodyAdvice;
import com.chh.advice.EncryptResponseBodyAdvice;
import com.chh.config.properties.SecretEncryptConfig;
import com.chh.handler.GlobSecurityExceptionHandler;
import com.chh.service.EncryptionService;
import com.chh.servlet.HttpServletRequestInputStreamFilter;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Import({
        HttpServletRequestInputStreamFilter.class,
        SecretEncryptConfig.class,
        EncryptResponseBodyAdvice.class,
        EncryptRequestBodyAdvice.class,
        GlobSecurityExceptionHandler.class,
        EncryptionService.class
})
public @interface EnableSecurityParameter {
}
