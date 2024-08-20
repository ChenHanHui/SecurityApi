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
public class SecurityException extends RuntimeException {

    private int code = -1;

    public SecurityException(int code) {
        this.code = code;
    }

    public SecurityException(String message) {
        super(message);
    }

    public SecurityException(int code, String message) {
        super(message);
        this.code = code;
    }

    public SecurityException(Throwable cause) {
        super(cause);
    }

    public SecurityException(String message, Throwable cause) {
        super(message, cause);
    }

    public int getCode() {
        return this.code;
    }

    public SecurityException setCode(int code) {
        this.code = code;
        return this;
    }

    public static void throwIf(boolean flag, int code, String message) {
        if (flag) {
            throw (new SecurityException(message)).setCode(code);
        }
    }

    public static void throwIfNull(Object value, int code, String message) {
        if (value == null) {
            throw (new SecurityException(message)).setCode(code);
        }
    }
}

