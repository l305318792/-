package com.rabbiter.hospital.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 数据源配置类
 * 使用Spring Boot 3.2.1的自动配置机制
 */
@Configuration
@EnableTransactionManagement
public class DataSourceConfig {
    // 使用Spring Boot的自动配置，无需手动配置数据源
}