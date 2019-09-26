package com.qf.springboot1905.shop_front2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class ShopFront2Application {

	public static void main(String[] args) {
		SpringApplication.run(ShopFront2Application.class, args);
	}

}
