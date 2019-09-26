package com.qf.springboot1905.search_service;

import com.alibaba.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@DubboComponentScan(value = "com.qf.service.impl")
@SpringBootApplication(scanBasePackages = "com.qf.listener",exclude = DataSourceAutoConfiguration.class)
public class SearchServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SearchServiceApplication.class, args);
    }

}
