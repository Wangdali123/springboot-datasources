/**
 * 
 */
/**
 * @ClassName package-info
 * @Description TODO
 * @author lide
 * @date 2018年2月9日 下午2:08:30
 */
package com.example.game.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.game.service.TestService;
/**
 * 
 * @ClassName YQSGameController
 * @Description TODO
 * @author lide
 * @date 2018年2月27日 下午4:15:08
 */
@RestController	//使用restcontroller requestmapping不需要responsebody 自动返回JSON格式
public class YQSGameController{
	
	protected static final Logger logger = LoggerFactory.getLogger(YQSGameController.class);
	
	@Autowired
	private TestService testService;
	
	@RequestMapping("/test")
	public String index() {
		logger.debug("测试信息：{}","welcome log world");
		return "主表："+testService.queryCountByMester();
	}
	
	@RequestMapping("/test1")
	public String test() {
		return "从表："+testService.queryCountBySavle();
	}
	
	
}