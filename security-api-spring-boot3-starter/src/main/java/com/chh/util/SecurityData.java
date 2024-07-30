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

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author 陈汉辉
 * @since 1.0.0
 */
public class SecurityData implements Serializable {

    @Serial
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
        if (!(o instanceof SecurityData that)) return false;
        return Objects.equals(getContent(), that.getContent());
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
