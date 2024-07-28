package com.chh.handler;

import com.chh.exception.SecurityBadException;
import com.chh.exception.SecurityException;
import com.chh.util.SecurityBuilder;
import com.chh.util.SecurityResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobSecurityExceptionHandler implements SecurityBuilder {

    @ExceptionHandler(value = SecurityException.class)
    public ResponseEntity<SecurityResult> SecurityExceptionHandler(SecurityException e) {
        return internalServer(e.getMessage());
    }

    @ExceptionHandler(value = SecurityBadException.class)
    public ResponseEntity<SecurityResult> SecurityBadExceptionHandler(SecurityBadException e) {
        return badRequest(e.getMessage());
    }

}
