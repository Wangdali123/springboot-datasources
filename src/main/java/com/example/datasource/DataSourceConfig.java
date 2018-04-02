package com.example.datasource;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * 数据源配置
 * 
 * @ClassName DataSourceConfig
 * @Description TODO
 * @author lide
 * @date 2018年2月27日 下午1:21:39
 */
@Configuration
public class DataSourceConfig {
	
	@Value("${mysql.datasource.type}")
	private Class<? extends DataSource> dataSourcePoolType;

	@Bean(name = "master")
	@ConfigurationProperties(prefix = "mysql.datasource.master") 
	public DataSource dataSource1() {
		return DataSourceBuilder.create().type(dataSourcePoolType).build();
	}

	@Bean(name = "slave")
	@ConfigurationProperties(prefix = "mysql.datasource.slave") 
	public DataSource dataSource2() {
		return DataSourceBuilder.create().type(dataSourcePoolType).build();
	}
	
	@Bean(name="dynamicDataSource")
	@Primary	//优先使用，多数据源
	public DataSource dataSource() {
		DynamicDataSource dynamicDataSource = new DynamicDataSource();
		DataSource master = dataSource1();
		DataSource slave = dataSource2();
		//设置默认数据源
		dynamicDataSource.setDefaultTargetDataSource(master);	
		//配置多数据源
		Map<Object,Object> map = new HashMap<>();
		map.put(DataSourceType.Master.getName(), master);	//key需要跟ThreadLocal中的值对应
		map.put(DataSourceType.Slave.getName(), slave);
		dynamicDataSource.setTargetDataSources(map);			
		return dynamicDataSource;
	}
	
}
