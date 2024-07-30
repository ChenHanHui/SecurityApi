package com.chh.demo;

import com.chh.annotation.EnableSecurityParameter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author 陈汉辉
 */
@SpringBootApplication
@EnableSecurityParameter
public class SecurityApiDemoSpringBoot3Application {

    public static void main(String[] args) {
        SpringApplication.run(SecurityApiDemoSpringBoot3Application.class, args);
    }

}
