package com.qf.springboot1905.shop_sso;


import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.entity.Email;
import com.qf.service.impl.IEmailService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ShopSsoApplicationTests {

    @Reference
    private IEmailService emailService;

    @Test
    public void testEmailCdoe() {

        for(int i =0;i<10;i++){
            //
            System.out.println((int)(Math.random()*10000)+1000);
        }

    }

    @Test
    public void test2(){

        int code = 1111;
        String content = "验证码:%d,如果非本人操作，请忽略.";

        System.out.println(String.format(content,code));


    }

    @Test
    public void testEamil(){
        Email email = new Email("小伙子在吗","我想问你个问题","844986057@qq.com");
        emailService.sendEmail(email);
    }


}
