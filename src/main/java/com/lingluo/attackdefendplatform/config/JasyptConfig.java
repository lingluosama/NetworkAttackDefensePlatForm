package com.lingluo.attackdefendplatform.config;

import org.jasypt.util.password.StrongPasswordEncryptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Jasypt 加密相关的 Spring 配置类。
 * 用于加密用户密码
 */
@Configuration
public class JasyptConfig {

    @Bean
    public StrongPasswordEncryptor strongPasswordEncryptor() {
        return new StrongPasswordEncryptor();
    }
}
