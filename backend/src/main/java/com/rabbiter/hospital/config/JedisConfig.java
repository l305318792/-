package com.rabbiter.hospital.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Redis配置类
 */
@Configuration
public class JedisConfig {
    @Value("${spring.data.redis.host:localhost}")
    private String host;
    @Value("${spring.data.redis.port:6379}")
    private int port;
    @Value("${spring.data.redis.password:}")
    private String password;
    @Value("${spring.data.redis.database:0}")
    private int database;
    @Value("${spring.data.redis.timeout:2000}")
    private int timeout;
    @Value("${spring.data.redis.jedis.pool.max-active:8}")
    private int maxTotal;
    @Value("${spring.data.redis.jedis.pool.max-idle:8}")
    private int maxIdle;
    @Value("${spring.data.redis.jedis.pool.min-idle:0}")
    private int minIdle;
    @Value("${spring.data.redis.jedis.pool.max-wait:-1}")
    private long maxWaitMillis;
    @Value("${spring.data.redis.jedis.pool.test-on-borrow:false}")
    private boolean testOnBorrow;

    /**
     * 配置JedisPoolConfig
     */
    @Bean
    public JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(maxTotal);
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMinIdle(minIdle);
        jedisPoolConfig.setMaxWait(java.time.Duration.ofMillis(maxWaitMillis));
        jedisPoolConfig.setTestOnBorrow(testOnBorrow);
        return jedisPoolConfig;
    }

    /**
     * 配置JedisPool
     */
    @Bean
    public JedisPool jedisPool(JedisPoolConfig jedisPoolConfig) {
        String pwd = password != null && !password.trim().isEmpty() ? password : null;
        return new JedisPool(jedisPoolConfig, host, port, timeout, pwd, database);
    }
}
