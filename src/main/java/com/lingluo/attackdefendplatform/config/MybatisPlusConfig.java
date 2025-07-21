package com.lingluo.attackdefendplatform.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration // 标记这是一个Spring配置类
public class MybatisPlusConfig {

    /**
     * 新的分页插件，一缓和二缓遵循mybatis的规则
     * 需要设置 MybatisPlusInterceptor 的 order 属性为 -1，确保在其他插件之前执行
     * 或者不设置 order，让其默认执行顺序
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }

}