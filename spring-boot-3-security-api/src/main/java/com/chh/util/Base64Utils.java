package com.chh.util;

import java.util.Base64;

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