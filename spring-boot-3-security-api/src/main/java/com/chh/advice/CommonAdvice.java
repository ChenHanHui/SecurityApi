package com.chh.advice;

import com.chh.annotation.SecurityParameter;
import com.chh.config.properties.SecretEncryptConfig;
import org.springframework.core.MethodParameter;

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

}
