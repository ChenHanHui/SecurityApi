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
package com.chh.handler;

import com.chh.exception.SecurityBadException;
import com.chh.exception.SecurityException;
import com.chh.util.SecurityBuilder;
import com.chh.util.SecurityResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author 陈汉辉
 * @since 1.0.0
 */
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
