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
package com.chh.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;

/**
 * @author 陈汉辉
 * @since 1.0.0
 */
@Configuration
@ConfigurationProperties(prefix = "security.encrypt")
public class SecretEncryptConfig {

    public enum Mode {
        RSA,
        AES,
        SM4
    }

    private Mode mode = Mode.RSA;

    @NestedConfigurationProperty
    private final RSA rsa;
    @NestedConfigurationProperty
    private final AES aes;
    @NestedConfigurationProperty
    private final SM4 sm4;
    private String charset = "UTF-8";
    private boolean inDecode = true;
    private boolean outEncode = true;
    private boolean showLog = true;
    private boolean cacheData = true;

    public SecretEncryptConfig() {
        this.rsa = new RSA();
        this.aes = new AES();
        this.sm4 = new SM4();
    }

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public RSA getRsa() {
        return rsa;
    }

    public AES getAes() {
        return aes;
    }

    public SM4 getSm4() {
        return sm4;
    }

    public String getCharset() {
        return this.charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public boolean isInDecode() {
        return inDecode;
    }

    public void setInDecode(boolean inDecode) {
        this.inDecode = inDecode;
    }

    public boolean isOutEncode() {
        return outEncode;
    }

    public void setOutEncode(boolean outEncode) {
        this.outEncode = outEncode;
    }

    public boolean isShowLog() {
        return this.showLog;
    }

    public void setShowLog(boolean showLog) {
        this.showLog = showLog;
    }

    public boolean isCacheData() {
        return cacheData;
    }

    public void setCacheData(boolean cacheData) {
        this.cacheData = cacheData;
    }
}
