package com.qf.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HelloController {

    @RequestMapping(value = "/hello")
    public String hello() {
        System.out.println("HelloController.hello");
        return "hello";
    }

    @RequestMapping(value = "/login")
    public String login(String username,String password){
        // 1.先判断是否登录
        Subject user = SecurityUtils.getSubject();
        if(!user.isAuthenticated()){
            UsernamePasswordToken token = new UsernamePasswordToken(username,password);
            try {
                user.login(token);
                return "index";
            }catch (AuthenticationException e){
                e.printStackTrace();
                return "login";
            }
        }
        return "hello";
    }

    @RequiresPermissions("user:add")
    @RequestMapping(value = "/add")
    public String add(){
        System.out.println("HelloController.add");
        return "index";
    }

}
