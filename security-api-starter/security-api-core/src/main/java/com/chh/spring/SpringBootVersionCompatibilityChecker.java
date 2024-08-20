package com.chh.spring;

import com.chh.util.StringUtils;
import org.springframework.boot.SpringBootVersion;

/**
 * SpringBoot 版本与 SecurityApi 版本兼容检查器
 *
 * @author 陈汉辉
 * @since 1.0.2
 */
public class SpringBootVersionCompatibilityChecker {

    public SpringBootVersionCompatibilityChecker() {
        String version = SpringBootVersion.getVersion();
        if (StringUtils.isEmpty(version) || version.startsWith("1.") || version.startsWith("2.")) {
            return;
        }
        String str = "当前 SpringBoot 版本（" + version + "）与 SecurityApi 依赖不兼容，" +
                "请将依赖 security-api-spring-boot-starter 修改为：security-api-spring-boot3-starter";
        System.err.println(str);
        throw new SecurityException(str);
    }

}
