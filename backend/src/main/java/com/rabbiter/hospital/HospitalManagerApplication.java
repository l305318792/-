package com.rabbiter.hospital;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 医院管理系统启动类
 * 配置包扫描和MyBatis Mapper扫描
 */
@MapperScan("com.rabbiter.hospital.mapper")
@SpringBootApplication
@EnableTransactionManagement
public class HospitalManagerApplication {
    
    /**
     * 应用程序入口
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        SpringApplication.run(HospitalManagerApplication.class, args);
    }
}
