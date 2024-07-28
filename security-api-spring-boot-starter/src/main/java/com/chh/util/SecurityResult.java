package com.chh.util;

import com.chh.converte.Convert;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

public class SecurityResult extends LinkedHashMap<String, Object> implements Serializable {

    private static final long serialVersionUID = 1L;

    public SecurityResult() {
    }

    public SecurityResult(int code, String message) {
        this.setCode(code);
        this.setMessage(message);
    }

    public SecurityResult(int code, String message, Boolean encryption, Object data) {
        this.setCode(code);
        this.setMessage(message);
        this.setEncryption(encryption);
        this.setData(data);
    }

    public SecurityResult(int code, String message, Boolean encryption, Object data, String sign) {
        this.setCode(code);
        this.setMessage(message);
        this.setEncryption(encryption);
        this.setData(data);
        this.setSign(sign);
    }

    public SecurityResult(Map<String, ?> map) {
        this.setMap(map);
    }

    public Integer getCode() {
        return (Integer) this.get("code");
    }

    public String getMessage() {
        return (String) this.get("message");
    }

    public Boolean getEncryption() {
        return (Boolean) this.get("encryption");
    }

    public Object getData() {
        return this.get("data");
    }

    public String getSign() {
        return (String) this.get("sign");
    }

    public SecurityResult setCode(int code) {
        this.put("code", code);
        return this;
    }

    public SecurityResult setMessage(String message) {
        this.put("message", message);
        return this;
    }

    public SecurityResult setEncryption(Boolean encryption) {
        this.put("encryption", encryption);
        return this;
    }

    public SecurityResult setData(Object data) {
        this.put("data", data);
        return this;
    }

    public SecurityResult setSign(String sign) {
        this.put("sign", sign);
        return this;
    }

    public SecurityResult set(String key, Object data) {
        this.put(key, data);
        return this;
    }

    public <T> T get(String key, Class<T> cs) {
        return Convert.getValueByType(this.get(key), cs);
    }

    public SecurityResult setMap(Map<String, ?> map) {
        for (String key : map.keySet()) {
            this.put(key, map.get(key));
        }
        return this;
    }

    public static SecurityResult ok() {
        return new SecurityResult(200, "ok");
    }

    public static SecurityResult ok(String message) {
        return new SecurityResult(200, message);
    }

    public static SecurityResult code(int code) {
        return new SecurityResult(code, null);
    }

    public static SecurityResult data(Object data) {
        return new SecurityResult(200, "ok", false, data);
    }

    public static SecurityResult data(Object data, Boolean encryption) {
        return new SecurityResult(200, "ok", encryption, data);
    }

    public static SecurityResult error() {
        return new SecurityResult(500, "error");
    }

    public static SecurityResult error(String message) {
        return new SecurityResult(500, message);
    }

    public static SecurityResult get(int code, String message, Boolean encryption, Object data, String sign) {
        return new SecurityResult(code, message, encryption, data, sign);
    }

    public String toString() {
        return "{\"code\": " + this.getCode() +
                ", \"message\": " + this.transValue(this.getMessage()) +
                ", \"encryption\": " + this.transValue(this.getEncryption()) +
                ", \"data\": " + this.transValue(this.getData()) +
                ", \"sign\": " + this.transValue(this.getSign()) +
                "}";
    }

    private String transValue(Object value) {
        if (value == null) {
            return null;
        } else {
            return value instanceof String ? "\"" +
                    value +
                    "\"" : String.valueOf(value);
        }
    }

}
