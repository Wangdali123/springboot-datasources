package com.example.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class RedisConfiguration {
	
	@Value("${spring.redis.host}")
	private String host;
	@Value("${spring.redis.port}")
	private int prot;
	@Value("${spring.redis.timeout}")
	private int timeout;
	@Value("${spring.redis.password}")
	private String password;
	
	@Bean(name= "jedisPool")  
    public JedisPool jedisPoolFactory() {
		//host, prot, timeout, password并非JedisPool的属性,是JedisFactory的属性
		//jedis构造方法最终是super(config,JedisFactory);
		//所以使用@ConfigurationProperties(prefix = "spring.redis")会导致获取不到属性值
		JedisPool jedisPool = new JedisPool(jedisPoolConfigFactory(), host, prot, timeout, password);
        return jedisPool;  
    } 	

	@Bean
	@ConfigurationProperties(prefix = "spring.redis.jedis.pool") 
	public JedisPoolConfig jedisPoolConfigFactory() {
		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		return jedisPoolConfig;
	}
 
}
