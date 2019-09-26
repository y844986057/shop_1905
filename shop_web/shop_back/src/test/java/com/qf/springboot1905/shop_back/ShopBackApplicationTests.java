package com.qf.springboot1905.shop_back;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class ShopBackApplicationTests {

	@Test
	public void contextLoads() {
		String  fileName ="xxxxx.png";
		String fileExtName = ""; // 文件类型
		// xxxxx.png
		fileExtName = fileName.substring(fileName.lastIndexOf(".")+1);
		System.out.println(fileExtName);
	}

}
