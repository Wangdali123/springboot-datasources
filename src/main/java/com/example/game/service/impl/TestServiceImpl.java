package com.example.game.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.datasource.DataSourceType;
import com.example.datasource.MyDataSource;
import com.example.game.domain.mapper.TestMapper;
import com.example.game.service.TestService;


/**
 * 
 * @ClassName TestServiceImpl
 * @Description TODO
 * @author lide
 * @date 2018年2月27日 下午4:14:57
 */
@Service
public class TestServiceImpl implements TestService {

	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private TestMapper testMapper;
	
	@Override
	@MyDataSource(DataSourceType.Master)
	public Integer queryCountByMester() {
		return testMapper.queryCount();
	}

	
	@Override
	@MyDataSource(DataSourceType.Slave)
	@Transactional
	public Integer queryCountBySavle() {
		//测试事务
		testMapper.updateAdminByName();
		Integer rows = testMapper.updateAdminByName();
		if(rows<=0) {	//更新小于1 执行回滚
			throw new RuntimeException();
		}
		return rows;
	}

}
