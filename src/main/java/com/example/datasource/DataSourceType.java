package com.example.datasource;

public enum DataSourceType {

	// 主表
	Master("master"),
	// 从表
	Slave("slave");

	private String name;

	private DataSourceType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
