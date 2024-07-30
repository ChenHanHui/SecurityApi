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
package com.chh.exception;

/**
 * @author 陈汉辉
 * @since 1.0.0
 */
public class SecurityBadException extends RuntimeException {

    private int code = -1;

    public SecurityBadException(int code) {
        this.code = code;
    }

    public SecurityBadException(String message) {
        super(message);
    }

    public SecurityBadException(int code, String message) {
        super(message);
        this.code = code;
    }

    public SecurityBadException(Throwable cause) {
        super(cause);
    }

    public SecurityBadException(String message, Throwable cause) {
        super(message, cause);
    }

    public int getCode() {
        return this.code;
    }

    public SecurityBadException setCode(int code) {
        this.code = code;
        return this;
    }

    public static void throwIf(boolean flag, int code, String message) {
        if (flag) {
            throw (new SecurityBadException(message)).setCode(code);
        }
    }

    public static void throwIfNull(Object value, int code, String message) {
        if (value == null) {
            throw (new SecurityBadException(message)).setCode(code);
        }
    }
}

