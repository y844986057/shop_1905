package com.qf.springboot1905.user_service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.entity.User;
import com.qf.service.impl.IUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceApplicationTests {

	@Reference
	private IUserService userService;

	@Test
	public void addUser() {

		for(int i =0;i<10;i++){
			User user = new User();
			user.setUsername("test_"+i);
			user.setPassword("pw_"+i);
			user.setAge(i+1);
			user.setSex(i % 2 ==0?1:0);
			user.setEmail("test_"+i+"@qf.com");
			user.setPhone("1345333333"+i);
			userService.add(user);
		}
	}

}
