package com.example.datasource;
/**
 * 动态数据源的上下文 threadlocal
 * @ClassName JdbcContextHolder
 * @Description TODO
 * @author lide
 * @date 2018年2月27日 上午11:43:34
 */
public class JdbcContextHolder {
	
	private final static ThreadLocal<String> local = new ThreadLocal<>();
	
	public static void putDataSource(String name) {
		local.set(name);
	}
	
	public static String getDataSource() {
		return local.get();
	}
	
}