package com.chh.util;

import java.io.Serializable;
import java.util.Objects;

public class SecurityData implements Serializable {

    private static final long serialVersionUID = 1L;

    // (AES加密、RSA加密加签)、base64编码之后的文本
    private String content;

    // 签名
    private String sign;

    // No-arg constructor required for Jackson deserialization
    public SecurityData() {
    }

    public SecurityData(String content, String sign) {
        this.content = content;
        this.sign = sign;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SecurityData)){
            return false;
        }
        return Objects.equals(getContent(), ((SecurityData) o).getContent());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getContent());
    }

    @Override
    public String toString() {
        return "SecurityData{" +
                "content='" + content + '\'' +
                ", sign='" + sign + '\'' +
                "}";
    }

}
