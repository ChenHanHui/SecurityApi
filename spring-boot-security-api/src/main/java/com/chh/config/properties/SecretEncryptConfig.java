package com.chh.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "security.encrypt")
public class SecretEncryptConfig {

    public enum Mode {
        RSA,
        AES
    }

    private Mode mode = Mode.RSA;

    @NestedConfigurationProperty
    private final RSA rsa;
    @NestedConfigurationProperty
    private final AES aes;
    private String charset = "UTF-8";
    private boolean inDecode = true;
    private boolean outEncode = true;
    private boolean showLog = true;

    public SecretEncryptConfig() {
        this.rsa = new RSA();
        this.aes = new AES();
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
}
