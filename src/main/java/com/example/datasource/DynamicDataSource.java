package com.example.datasource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 
 * @ClassName DynamicDataSource
 * @Description TODO
 * @author lide
 * @date 2018年2月27日 上午9:34:00
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Override
	protected Object determineCurrentLookupKey() {
		logger.info("数据源为{}",JdbcContextHolder.getDataSource());
		return JdbcContextHolder.getDataSource();
	}
	
}