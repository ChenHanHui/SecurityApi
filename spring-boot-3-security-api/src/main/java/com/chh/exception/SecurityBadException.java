package com.chh.exception;

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

