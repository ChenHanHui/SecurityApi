package com.chh.demo.handler;

import com.chh.util.SecurityBuilder;
import com.chh.util.SecurityResult;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * @author 陈汉辉
 */
@RestControllerAdvice
public class GlobExceptionHandler implements SecurityBuilder {

    /**
     * 参数为空异常处理
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<SecurityResult> handleConstraintViolationException(ConstraintViolationException e, HttpServletRequest request) {
        return badRequest("缺少必需的参数: " + e.getMessage());
    }

    /**
     * 参数校验异常处理
     */
    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    public ResponseEntity<SecurityResult> handleMethodArgumentNotValidException(Exception e, HttpServletRequest request) {
        String responseMessage;
        if (e instanceof MethodArgumentNotValidException) {
            FieldError fieldError = ((MethodArgumentNotValidException) e).getBindingResult().getFieldError();
            if (fieldError != null) {
                responseMessage = fieldError.getDefaultMessage();
            } else {
                responseMessage = "参数校验不匹配";
            }
        } else if (e instanceof BindException) {
            FieldError fieldError = ((BindException) e).getBindingResult().getFieldError();
            if (fieldError != null) {
                responseMessage = fieldError.getDefaultMessage();
            } else {
                responseMessage = "参数校验不匹配";
            }
        } else {
            responseMessage = "参数校验不匹配";
        }
        return badRequest(responseMessage);
    }

    /**
     * 参数类型异常处理
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<SecurityResult> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e, HttpServletRequest request) {
        return badRequest("参数类型不匹配: " + e.getMessage());
    }

}
