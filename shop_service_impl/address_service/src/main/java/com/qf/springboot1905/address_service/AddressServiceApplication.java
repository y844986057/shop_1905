package com.qf.springboot1905.address_service;


import com.alibaba.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@DubboComponentScan(basePackages = "com.qf.service.impl")
@MapperScan(basePackages = "com.qf.dao")
@SpringBootApplication
public class AddressServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AddressServiceApplication.class, args);
    }

}
