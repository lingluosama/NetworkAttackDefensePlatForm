package com.lingluo.attackdefendplatform.service.impl;


import com.lingluo.attackdefendplatform.service.PasswordEncryptionService;
import lombok.AllArgsConstructor;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PasswordEncryptionServiceImpl implements PasswordEncryptionService {
    private final StrongPasswordEncryptor jasyptEncryptor;
    


    @Override
    public String encrypt(String rawPassword) {
        if (rawPassword == null || rawPassword.isEmpty()) {
            throw new IllegalArgumentException("原始密码不能为空或null。");
        }
        return jasyptEncryptor.encryptPassword(rawPassword);
    }


    @Override
    public boolean matches(String rawPassword, String encryptedPassword) {
        if (rawPassword == null || rawPassword.isEmpty() || encryptedPassword == null || encryptedPassword.isEmpty()) {
            return false; // 任何一个为空都认为不匹配，或者可以根据业务需求抛出异常
        }
        return jasyptEncryptor.checkPassword(rawPassword, encryptedPassword);
    }
}
