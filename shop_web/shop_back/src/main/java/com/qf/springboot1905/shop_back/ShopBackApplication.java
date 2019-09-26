package com.qf.springboot1905.shop_back;

import com.github.tobato.fastdfs.FdfsClientConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.context.annotation.Import;
import org.springframework.jmx.support.RegistrationPolicy;

import javax.sql.DataSource;

@Import(FdfsClientConfig.class)
@SpringBootApplication(scanBasePackages = "com.qf",exclude = DataSourceAutoConfiguration.class)
@EnableMBeanExport(registration = RegistrationPolicy.IGNORE_EXISTING)
public class ShopBackApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShopBackApplication.class, args);
	}

}
