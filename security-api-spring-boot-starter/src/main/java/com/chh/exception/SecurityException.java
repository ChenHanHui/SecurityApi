package com.chh.exception;

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

