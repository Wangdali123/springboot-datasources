package com.example.demo;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.junit.Test;

public class TestSE {

	@Test
	public void test() {
		String str = "[1]23ASDZXC[1]23";
		System.out.println(str.replaceFirst("\\[1]", "9"));
	}
	
	
	@Test
	public void iterator() throws InterruptedException {
		Map<Integer,String> map = new HashMap<>();
		for(int i=0;i<10;i++) {
			map.put(i, i+"-value");
		}
		
		Iterator<Integer> iterator = map.keySet().iterator();
		new Thread(()->{
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			map.remove(7);
			map.remove(8);
		}).start();
		while(iterator.hasNext()) {
			Thread.sleep(500);
			System.out.println(iterator.next());
		}
		System.out.println(map);
	}
	
	
}
