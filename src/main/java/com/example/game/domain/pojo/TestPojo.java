/**
 * 
 */
/**
 * @ClassName package-info
 * @Description TODO
 * @author lide
 * @date 2018年2月9日 下午4:22:34
 */
package com.example.game.domain.pojo;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

public class TestPojo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String name;
	
	private Integer age;
	
	private String sex;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	
	
}