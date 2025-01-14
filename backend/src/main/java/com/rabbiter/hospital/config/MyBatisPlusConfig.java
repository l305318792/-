package com.rabbiter.hospital.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * MyBatis Plus配置类
 * 用于配置MyBatis Plus的各种插件和行为
 */
@EnableTransactionManagement
@Configuration
public class MyBatisPlusConfig {
    
    /**
     * 配置MyBatis Plus插件
     * 包含分页插件、乐观锁插件和防全表更新与删除插件
     *
     * @return MybatisPlusInterceptor 配置好的插件拦截器
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        
        // 添加分页插件
        PaginationInnerInterceptor pageInterceptor = new PaginationInnerInterceptor();
        pageInterceptor.setDbType(DbType.MYSQL);
        pageInterceptor.setOverflow(true);
        pageInterceptor.setMaxLimit(1000L); // 设置最大单页限制数量
        interceptor.addInnerInterceptor(pageInterceptor);
        
        // 乐观锁插件
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        
        // 防止全表更新与删除插件
        interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());
        
        return interceptor;
    }
}
