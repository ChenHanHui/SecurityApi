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

import java.util.Base64;

/**
 * @author 陈汉辉
 * @since 1.0.0
 */
public abstract class Base64Utils {
    public Base64Utils() {
    }

    public static byte[] encode(byte[] src) {
        return src.length == 0 ? src : Base64.getEncoder().encode(src);
    }

    public static byte[] decode(byte[] src) {
        return src.length == 0 ? src : Base64.getDecoder().decode(src);
    }

    public static byte[] encodeUrlSafe(byte[] src) {
        return src.length == 0 ? src : Base64.getUrlEncoder().encode(src);
    }

    public static byte[] decodeUrlSafe(byte[] src) {
        return src.length == 0 ? src : Base64.getUrlDecoder().decode(src);
    }

    public static String encodeToString(byte[] src) {
        return src.length == 0 ? "" : Base64.getEncoder().encodeToString(src);
    }

    public static byte[] decodeFromString(String src) {
        return src.isEmpty() ? new byte[0] : Base64.getDecoder().decode(src);
    }

    public static String encodeToUrlSafeString(byte[] src) {
        return Base64.getUrlEncoder().encodeToString(src);
    }

    public static byte[] decodeFromUrlSafeString(String src) {
        return Base64.getUrlDecoder().decode(src);
    }
}
