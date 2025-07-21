package com.lingluo.attackdefendplatform.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SaTokenConfigure implements WebMvcConfigurer {
    /**
     * 注册 Sa-Token 的拦截器，打开注解式鉴权功能
     * 注意:不会主动监测token有效性，记得使用@SaCheckLogin监测登录状态
     * 这里的路由只是声明了SaToken的管辖范围
     */
    private static final String BASEURL="/api/v1/defense";
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册 Sa-Token 拦截器，定义拦截规则
        registry.addInterceptor(new SaInterceptor()) // 传入 new SaInterceptor()，它会自动处理注解
                .addPathPatterns("/**")//管辖所有路由
                .excludePathPatterns(//放行路由配置
                        BASEURL+"/member/register",
                        BASEURL+"/member/login", 
                        BASEURL+"/member/logout"                );
    }
}