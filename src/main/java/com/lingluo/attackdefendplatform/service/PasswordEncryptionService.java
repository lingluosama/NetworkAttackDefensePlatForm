package com.lingluo.attackdefendplatform.service;

public interface PasswordEncryptionService {
    /**
     * 对原始密码进行加密。
     *
     * @param rawPassword 原始密码
     * @return 加密后的密码字符串
     * @throws IllegalArgumentException 如果原始密码为空或null
     */
    String encrypt(String rawPassword);

    /**
     * 验证原始密码与已加密的密码是否匹配。
     *
     * @param rawPassword 原始密码（用户输入）
     * @param encryptedPassword 已加密的密码（从数据库中获取）
     * @return 如果匹配则返回 true，否则返回 false
     */
    boolean matches(String rawPassword, String encryptedPassword);
}
