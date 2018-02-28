///**
// * 
// */
///**
// * @ClassName package-info
// * @Description TODO
// * @author lide
// * @date 2018年2月9日 下午4:01:18
// */
//package com.example.filter;
//
//import java.io.IOException;
//
//import javax.servlet.Filter;
//import javax.servlet.FilterChain;
//import javax.servlet.FilterConfig;
//import javax.servlet.ServletException;
//import javax.servlet.ServletRequest;
//import javax.servlet.ServletResponse;
//import javax.servlet.annotation.WebFilter;
//
//import org.springframework.boot.web.servlet.ServletComponentScan;
//import org.springframework.context.annotation.Configuration;
//
///**
// * 自定义过滤器
// * @ClassName TestFilter
// * @Description TODO
// * @author lide
// * @date 2018年2月9日 下午4:02:31
// */
//@Configuration
//@ServletComponentScan
//@WebFilter(urlPatterns="/test",filterName="testFilter")
//public class TestFilter implements Filter{
//
//	@Override
//	public void destroy() {
//		
//	}
//
//	@Override
//	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filter)
//			throws IOException, ServletException {
//		System.out.println("过滤开始");
//		String name = request.getParameter("name");
//		if(name==null) {
//			System.out.println("无效参数,请求终止");
//			return;
//		}
//		filter.doFilter(request, response);
//	}
//
//	@Override
//	public void init(FilterConfig arg0) throws ServletException {
//		// TODO Auto-generated method stub
//		
//	}
//	
//}