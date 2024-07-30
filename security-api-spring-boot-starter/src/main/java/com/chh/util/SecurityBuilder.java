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
package com.chh.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * @author 陈汉辉
 * @since 1.0.0
 */
public interface SecurityBuilder {

    /**
     * 成功的构造
     *
     * @param data 数据
     * @return Result
     */
    default ResponseEntity<SecurityResult> success(Object data) {
        return ResponseEntity.ok(
                SecurityResult.data(data)
        );
    }

    /**
     * 400的构造
     *
     * @param errorMsg 错误信息
     * @return Result
     */
    default ResponseEntity<SecurityResult> badRequest(String errorMsg) {
        return ResponseEntity.ok()
                .body(SecurityResult.code(HttpStatus.BAD_REQUEST.value())
                        .setMessage(errorMsg)
                );
    }

    /**
     * 400的构造(状态码错误)
     *
     * @param errorMsg 错误信息
     * @return Result
     */
    default ResponseEntity<SecurityResult> badRequestError(String errorMsg) {
        return ResponseEntity.badRequest()
                .body(SecurityResult.code(HttpStatus.BAD_REQUEST.value())
                        .setMessage(errorMsg)
                );
    }

    /**
     * 404的构造
     *
     * @param errorMsg 错误信息
     * @return Result
     */
    default ResponseEntity<SecurityResult> notFound(String errorMsg) {
        return ResponseEntity.ok()
                .body(SecurityResult.code(HttpStatus.NOT_FOUND.value())
                        .setMessage(errorMsg));
    }

    /**
     * 404的构造(状态码错误)
     *
     * @param errorMsg 错误信息
     * @return Result
     */
    default ResponseEntity<SecurityResult> notFoundError(String errorMsg) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND.value())
                .body(SecurityResult.code(HttpStatus.NOT_FOUND.value())
                        .setMessage(errorMsg));
    }

    /**
     * 500的构造
     *
     * @param errorMsg 错误信息
     * @return Result
     */
    default ResponseEntity<SecurityResult> internalServer(String errorMsg) {
        return ResponseEntity.ok()
                .body(SecurityResult.code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .setMessage(errorMsg)
                );
    }

    /**
     * 500的构造(状态码错误)
     *
     * @param errorMsg 错误信息
     * @return Result
     */
    default ResponseEntity<SecurityResult> internalServerError(String errorMsg) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .body(SecurityResult.code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .setMessage(errorMsg)
                );
    }

}
