package com.lingluo.attackdefendplatform.common.constant;

/**
 * 缓存常量
 *
 * @author haoxr
 * @since 2023/11/24
 */
public interface SecurityConstants {

    /**
     * 验证码缓存前缀
     */
    String CAPTCHA_CODE_PREFIX = "captcha_code:";

    /**
     * 角色和权限缓存前缀
     */
    String ROLE_PERMS_PREFIX = "role_perms:";

    /**
     * 黑名单Token缓存前缀
     */
    String BLACKLIST_TOKEN_PREFIX = "token:blacklist:";

    /**
     * 系统证书缓存
     */
    String SYSTEM_CERTIFICATE = "system:certificate";


    /**
     * 登录路径
     */
    String LOGIN_PATH = "/api/v1/auth/login";
    String LOGIN_PATH_USERNAME = "/api/v1/auth/sso/login";
    String LOGIN_PATH_SMS = "/api/v1/auth/loginSMS";


    /**
     * JWT Token 前缀
     */
    String JWT_TOKEN_PREFIX = "Bearer ";


}
