package com.greendam.template.common.utils;

/**
 * 密码加解密工具类（基于 jBCrypt 实现）
 */
public class PasswordUtils {
    private static final int DEFAULT_LOG_ROUNDS = 10;

    /**
     * 对明文密码进行 bcrypt 加密
     * @param rawPassword 明文密码
     * @return bcrypt 哈希
     */
    public static String hashPassword(String rawPassword) {
        if (rawPassword == null) {
            return null;
        }
        return BCrypt.hashpw(rawPassword, BCrypt.gensalt(DEFAULT_LOG_ROUNDS));
    }

    /**
     * 校验明文密码与 bcrypt 哈希是否匹配
     * @param rawPassword 明文密码
     * @param hashedPassword bcrypt 哈希
     * @return 是否匹配
     */
    public static boolean matches(String rawPassword, String hashedPassword) {
        if (rawPassword == null || hashedPassword == null) {
            return false;
        }
        return BCrypt.checkpw(rawPassword, hashedPassword);
    }
}


