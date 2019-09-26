package com.qf.springboot1905.shop_search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(scanBasePackages = "com.qf",exclude = DataSourceAutoConfiguration.class)
public class ShopSearchApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShopSearchApplication.class, args);
	}

}
